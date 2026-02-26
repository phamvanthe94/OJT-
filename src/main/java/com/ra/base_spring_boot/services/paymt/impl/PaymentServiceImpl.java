package com.ra.base_spring_boot.services.paymt.impl;

import com.ra.base_spring_boot.dto.resp.PayPalCreateOrderResponse;
import com.ra.base_spring_boot.dto.resp.payment.PayPalPaymentResultResponse;
import com.ra.base_spring_boot.model.constants.PaymentMethod;
import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.booking.Booking;
import com.ra.base_spring_boot.model.entity.booking.Payment;
import com.ra.base_spring_boot.model.entity.booking.PaymentProvider;
import com.ra.base_spring_boot.repository.booking.IBookingRepository;
import com.ra.base_spring_boot.repository.payment.IPaymentProviderRepository;
import com.ra.base_spring_boot.repository.payment.PaymentRepository;
import com.ra.base_spring_boot.services.paymt.IPayPalClientService;
import com.ra.base_spring_boot.services.paymt.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements IPaymentService {

    private final IPayPalClientService payPalClientService;
    private final PaymentRepository paymentRepository;
    private final IBookingRepository bookingRepository;
    private final IPaymentProviderRepository paymentProviderRepository;

    @Value("${paypal.return-url}")
    private String returnUrl;

    @Value("${paypal.cancel-url}")
    private String cancelUrl;

    // ✅ Tính tiền JPY từ booking (JPY không có số lẻ)
    private BigDecimal calcAmountJpy(Booking booking) {
        if (booking == null || booking.getTotalPriceMovie() == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal raw = BigDecimal.valueOf(booking.getTotalPriceMovie());
        return raw.setScale(0, RoundingMode.HALF_UP);
    }

    // ✅ format amount cho PayPal theo currency
    private String formatPayPalAmount(String currency, BigDecimal amount) {
        if (amount == null) amount = BigDecimal.ZERO;

        if ("JPY".equalsIgnoreCase(currency)) {
            return amount.setScale(0, RoundingMode.HALF_UP).toPlainString();
        }
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    @Override
    public PayPalCreateOrderResponse createPayPalOrder(Long bookingId) {

        // 1) Lấy booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy booking!"));

        // ✅ CHẶN 1: Nếu booking đã hoàn tất thanh toán => không cho thanh toán lại
        // (nếu Booking của bạn có field paymentStatus)
        if (booking.getPaymentStatus() == PaymentStatus.COMPLETED) {
            throw new RuntimeException("Booking này đã thanh toán thành công, không thể thanh toán lại.");
        }

        // ✅ CHẶN 2: Nếu trong bảng payments đã có 1 payment COMPLETED cho booking này => chặn
        if (paymentRepository.existsByBooking_IdAndPaymentStatus(bookingId, PaymentStatus.COMPLETED)) {
            throw new RuntimeException("Booking này đã có giao dịch thanh toán thành công, không thể thanh toán lại.");
        }

        // ✅ CHẶN 3 (khuyến nghị): Nếu đang có payment PENDING => tránh bấm nhiều lần tạo nhiều order
        if (paymentRepository.existsByBooking_IdAndPaymentStatusIn(
                bookingId, List.of(PaymentStatus.PENDING))) {
            throw new RuntimeException("Booking đang có giao dịch thanh toán đang chờ (PENDING). Vui lòng hoàn tất hoặc huỷ.");
        }

        // ✅ tiền thật theo booking
        BigDecimal amount = calcAmountJpy(booking);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Số tiền thanh toán không hợp lệ (<=0).");
        }

        // 2) Lấy provider PAYPAL (status=true)
        PaymentProvider provider = paymentProviderRepository.findByProviderCode("PAYPAL")
                .filter(PaymentProvider::getStatus)
                .orElseThrow(() -> new RuntimeException("PaymentProvider PAYPAL không tồn tại hoặc đã bị disable"));

        // 3) Tạo payment PENDING
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setProvider(provider);
        payment.setPaymentMethod(PaymentMethod.PAYPAL);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setAmount(amount);
        payment = paymentRepository.save(payment);

        // 4) Tạo order bên PayPal (JPY)
        String token = payPalClientService.getAccessToken();
        String customId = "BOOKING_" + bookingId;

        String currency = "JPY";
        String amountStr = formatPayPalAmount(currency, amount);

        Map<String, Object> order = payPalClientService.createOrder(
                token,
                returnUrl,
                cancelUrl,
                currency,
                amountStr,
                customId
        );

        String orderId = (String) order.get("id");
        String approveUrl = extractApproveUrl(order);

        // 5) Lưu paypalOrderId để map return/webhook
        payment.setPaypalOrderId(orderId);
        paymentRepository.save(payment);

        return PayPalCreateOrderResponse.builder()
                .paymentId(payment.getId())
                .bookingId(bookingId)
                .orderId(orderId)
                .approveUrl(approveUrl)
                .status(payment.getPaymentStatus().name())
                .amount(amount.doubleValue())
                .currency(currency)
                .build();
    }

    @Override
    public PayPalPaymentResultResponse captureFromReturn(String orderId) {

        Payment payment = paymentRepository.findByPaypalOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for orderId=" + orderId));

        Booking booking = payment.getBooking();
        Long bookingId = booking != null ? booking.getId() : null;

        // ✅ Nếu booking đã COMPLETED rồi thì return luôn (chặn xử lý lại)
        if (booking != null && booking.getPaymentStatus() == PaymentStatus.COMPLETED) {
            return PayPalPaymentResultResponse.builder()
                    .paymentId(payment.getId())
                    .bookingId(bookingId)
                    .paymentStatus(PaymentStatus.COMPLETED.name())
                    .transactionId(payment.getTransactionId())
                    .amount(payment.getAmount().doubleValue())
                    .currency("JPY")
                    .message("Booking đã thanh toán trước đó, không xử lý lại.")
                    .build();
        }

        // Nếu webhook đã cập nhật payment trước
        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
            // ✅ đồng bộ booking luôn cho chắc
            if (booking != null) {
                booking.setPaymentStatus(PaymentStatus.COMPLETED);
                bookingRepository.save(booking);
            }

            return PayPalPaymentResultResponse.builder()
                    .paymentId(payment.getId())
                    .bookingId(bookingId)
                    .paymentStatus(PaymentStatus.COMPLETED.name())
                    .transactionId(payment.getTransactionId())
                    .amount(payment.getAmount().doubleValue())
                    .currency("JPY")
                    .message("Thanh toán đã được xác nhận (webhook).")
                    .build();
        }

        // Fallback capture tại return
        String token = payPalClientService.getAccessToken();
        Map<String, Object> captureResp = payPalClientService.captureOrder(token, orderId);

        String status = (String) captureResp.get("status");
        if ("COMPLETED".equalsIgnoreCase(status)) {
            String captureId = extractCaptureId(captureResp);

            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            payment.setPaymentTime(LocalDateTime.now());
            payment.setPaypalCaptureId(captureId);
            payment.setTransactionId(captureId);
            paymentRepository.save(payment);

            // ✅ QUAN TRỌNG: update luôn booking để lần sau createPayPalOrder chặn được
            if (booking != null) {
                booking.setPaymentStatus(PaymentStatus.COMPLETED);
                bookingRepository.save(booking);
            }

            return PayPalPaymentResultResponse.builder()
                    .paymentId(payment.getId())
                    .bookingId(bookingId)
                    .paymentStatus(PaymentStatus.COMPLETED.name())
                    .transactionId(captureId)
                    .amount(payment.getAmount().doubleValue())
                    .currency("JPY")
                    .message("Thanh toán thành công (return capture).")
                    .build();
        }

        payment.setPaymentStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

        return PayPalPaymentResultResponse.builder()
                .paymentId(payment.getId())
                .bookingId(bookingId)
                .paymentStatus(PaymentStatus.FAILED.name())
                .transactionId(null)
                .amount(payment.getAmount().doubleValue())
                .currency("JPY")
                .message("Thanh toán thất bại hoặc chưa hoàn tất.")
                .build();
    }

    @SuppressWarnings("unchecked")
    private String extractApproveUrl(Map<String, Object> order) {
        List<Map<String, Object>> links = (List<Map<String, Object>>) order.get("links");
        return links.stream()
                .filter(l -> "approve".equals(l.get("rel")))
                .map(l -> (String) l.get("href"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No approve link"));
    }

    @SuppressWarnings("unchecked")
    private String extractCaptureId(Map<String, Object> capture) {
        List<Map<String, Object>> pus = (List<Map<String, Object>>) capture.get("purchase_units");
        Map<String, Object> pu0 = pus.get(0);
        Map<String, Object> payments = (Map<String, Object>) pu0.get("payments");
        List<Map<String, Object>> captures = (List<Map<String, Object>>) payments.get("captures");
        return (String) captures.get(0).get("id");
    }
}