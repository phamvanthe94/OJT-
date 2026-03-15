package com.ra.base_spring_boot.services.paymt.impl;

import com.ra.base_spring_boot.dto.resp.payment.PayPalCreateOrderResponse;
import com.ra.base_spring_boot.dto.resp.payment.PayPalPaymentResultResponse;
import com.ra.base_spring_boot.exception.HttpBadRequest;
import com.ra.base_spring_boot.exception.HttpConflict;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.constants.BookingStatus;
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
import com.ra.base_spring_boot.services.paymt.ITicketIssuanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class PaymentServiceImpl implements IPaymentService {

    private static final String PAYPAL_PROVIDER_CODE = "PAYPAL";
    private static final String CURRENCY_JPY = "JPY";

    private final IPayPalClientService payPalClientService;
    private final PaymentRepository paymentRepository;
    private final IBookingRepository bookingRepository;
    private final IPaymentProviderRepository paymentProviderRepository;
    private final ITicketIssuanceService ticketIssuanceService;

    @Value("${paypal.return-url}")
    private String returnUrl;

    @Value("${paypal.cancel-url}")
    private String cancelUrl;

    @Override
    public void cancelByOrderId(String orderId) {
        Payment payment = paymentRepository.findByPaypalOrderId(orderId)
                .orElseThrow(() -> new HttpNotFound("Payment not found"));

        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
            return;
        }

        payment.setPaymentStatus(PaymentStatus.CANCELLED);
        payment.setPaymentTime(LocalDateTime.now());
        syncBookingStatus(payment.getBooking(), BookingStatus.CANCELLED, PaymentStatus.CANCELLED);
        paymentRepository.save(payment);
    }

    @Override
    public PayPalCreateOrderResponse createPayPalOrder(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new HttpNotFound("Booking not found"));

        if (paymentRepository.existsByBooking_IdAndPaymentStatus(bookingId, PaymentStatus.COMPLETED)) {
            throw new HttpConflict("Booking already has a completed payment");
        }

        Payment pending = paymentRepository.findByBooking_Id(bookingId)
                .filter(payment -> payment.getPaymentStatus() == PaymentStatus.PENDING)
                .orElse(null);

        if (pending != null) {
            return buildPendingPaymentResponse(bookingId, pending);
        }

        BigDecimal amount = calculateJpyAmount(booking);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new HttpBadRequest("Payment amount must be greater than zero");
        }

        PaymentProvider provider = paymentProviderRepository.findByProviderCode(PAYPAL_PROVIDER_CODE)
                .filter(PaymentProvider::getStatus)
                .orElseThrow(() -> new HttpNotFound("PayPal payment provider is not available"));

        Payment payment = createPendingPayment(booking, provider, amount);
        Map<String, Object> order = createPayPalOrder(bookingId, amount);

        String orderId = (String) order.get("id");
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalStateException("PayPal order response does not contain an id");
        }

        payment.setPaypalOrderId(orderId);
        paymentRepository.save(payment);

        return PayPalCreateOrderResponse.builder()
                .paymentId(payment.getId())
                .bookingId(bookingId)
                .orderId(orderId)
                .approveUrl(extractApproveUrl(order))
                .status(payment.getPaymentStatus().name())
                .amount(amount.doubleValue())
                .currency(CURRENCY_JPY)
                .build();
    }

    @Override
    public PayPalPaymentResultResponse captureFromReturn(String orderId) {
        Payment payment = paymentRepository.findByPaypalOrderId(orderId)
                .orElseThrow(() -> new HttpNotFound("Payment not found"));

        Booking booking = payment.getBooking();
        Long bookingId = booking != null ? booking.getId() : null;

        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
            syncBookingStatus(booking, BookingStatus.COMPLETED, PaymentStatus.COMPLETED);
            return buildPaymentResult(payment, bookingId, payment.getTransactionId(), "Payment was already completed");
        }

        String token = payPalClientService.getAccessToken();
        Map<String, Object> captureResponse = payPalClientService.captureOrder(token, orderId);
        String status = (String) captureResponse.get("status");

        if ("COMPLETED".equalsIgnoreCase(status)) {
            String captureId = extractCaptureId(captureResponse);
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            payment.setPaymentTime(LocalDateTime.now());
            payment.setPaypalCaptureId(captureId);
            payment.setTransactionId(captureId);
            syncBookingStatus(booking, BookingStatus.COMPLETED, PaymentStatus.COMPLETED);
            paymentRepository.save(payment);

            ticketIssuanceService.issueTicketAfterPaymentCompleted(payment);

            return buildPaymentResult(payment, bookingId, captureId, "Payment completed successfully");
        }

        payment.setPaymentStatus(PaymentStatus.FAILED);
        syncBookingStatus(booking, BookingStatus.FAILED, PaymentStatus.FAILED);
        paymentRepository.save(payment);

        return PayPalPaymentResultResponse.builder()
                .paymentId(payment.getId())
                .bookingId(bookingId)
                .paymentStatus(PaymentStatus.FAILED.name())
                .transactionId(null)
                .amount(payment.getAmount() != null ? payment.getAmount().doubleValue() : 0D)
                .currency(CURRENCY_JPY)
                .message("Payment failed or was not completed")
                .build();
    }

    private PayPalCreateOrderResponse buildPendingPaymentResponse(Long bookingId, Payment pending) {
        if (pending.getPaypalOrderId() == null || pending.getPaypalOrderId().isBlank()) {
            throw new HttpConflict("Pending payment is missing PayPal order id");
        }

        String token = payPalClientService.getAccessToken();
        Map<String, Object> order = payPalClientService.getOrder(token, pending.getPaypalOrderId());

        return PayPalCreateOrderResponse.builder()
                .paymentId(pending.getId())
                .bookingId(bookingId)
                .orderId(pending.getPaypalOrderId())
                .approveUrl(extractApproveUrl(order))
                .status(pending.getPaymentStatus().name())
                .amount(pending.getAmount() != null ? pending.getAmount().doubleValue() : 0D)
                .currency(CURRENCY_JPY)
                .build();
    }

    private Payment createPendingPayment(Booking booking, PaymentProvider provider, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setProvider(provider);
        payment.setPaymentMethod(PaymentMethod.PAYPAL);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setAmount(amount);
        return paymentRepository.save(payment);
    }

    private Map<String, Object> createPayPalOrder(Long bookingId, BigDecimal amount) {
        String token = payPalClientService.getAccessToken();
        return payPalClientService.createOrder(
                token,
                returnUrl,
                cancelUrl,
                CURRENCY_JPY,
                formatPayPalAmount(CURRENCY_JPY, amount),
                "BOOKING_" + bookingId
        );
    }

    private BigDecimal calculateJpyAmount(Booking booking) {
        if (booking == null || booking.getTotalAmount() == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(booking.getTotalAmount()).setScale(0, RoundingMode.HALF_UP);
    }

    private String formatPayPalAmount(String currency, BigDecimal amount) {
        BigDecimal normalizedAmount = amount == null ? BigDecimal.ZERO : amount;
        if (CURRENCY_JPY.equalsIgnoreCase(currency)) {
            return normalizedAmount.setScale(0, RoundingMode.HALF_UP).toPlainString();
        }
        return normalizedAmount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    @SuppressWarnings("unchecked")
    private String extractApproveUrl(Map<String, Object> order) {
        List<Map<String, Object>> links = (List<Map<String, Object>>) order.get("links");
        if (links == null) {
            throw new IllegalStateException("PayPal order response does not contain links");
        }

        return links.stream()
                .filter(link -> "approve".equals(link.get("rel")))
                .map(link -> (String) link.get("href"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("PayPal order response does not contain approve link"));
    }

    @SuppressWarnings("unchecked")
    private String extractCaptureId(Map<String, Object> capture) {
        List<Map<String, Object>> purchaseUnits = (List<Map<String, Object>>) capture.get("purchase_units");
        if (purchaseUnits == null || purchaseUnits.isEmpty()) {
            throw new IllegalStateException("PayPal capture response does not contain purchase units");
        }

        Map<String, Object> payments = (Map<String, Object>) purchaseUnits.get(0).get("payments");
        if (payments == null) {
            throw new IllegalStateException("PayPal capture response does not contain payments");
        }

        List<Map<String, Object>> captures = (List<Map<String, Object>>) payments.get("captures");
        if (captures == null || captures.isEmpty()) {
            throw new IllegalStateException("PayPal capture response does not contain captures");
        }

        String captureId = (String) captures.get(0).get("id");
        if (captureId == null || captureId.isBlank()) {
            throw new IllegalStateException("PayPal capture response does not contain capture id");
        }
        return captureId;
    }

    private PayPalPaymentResultResponse buildPaymentResult(
            Payment payment,
            Long bookingId,
            String transactionId,
            String message
    ) {
        return PayPalPaymentResultResponse.builder()
                .paymentId(payment.getId())
                .bookingId(bookingId)
                .paymentStatus(payment.getPaymentStatus().name())
                .transactionId(transactionId)
                .amount(payment.getAmount() != null ? payment.getAmount().doubleValue() : 0D)
                .currency(CURRENCY_JPY)
                .message(message)
                .build();
    }

    private void syncBookingStatus(Booking booking, BookingStatus bookingStatus, PaymentStatus paymentStatus) {
        if (booking == null) {
            return;
        }
        booking.setStatus(bookingStatus);
        booking.setPaymentStatus(paymentStatus);
        bookingRepository.save(booking);
    }
}
