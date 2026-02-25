package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.ChoosePaymentSelectionDTO;
import com.ra.base_spring_boot.dto.req.CompleteBookingDTO;
import com.ra.base_spring_boot.model.constants.PaymentMethod;
import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.booking.Booking;
import com.ra.base_spring_boot.model.entity.booking.BookingSeat;
import com.ra.base_spring_boot.model.entity.booking.Payment;
import com.ra.base_spring_boot.model.entity.booking.PaymentProvider;
import com.ra.base_spring_boot.repository.booking.IBookingRepository;
import com.ra.base_spring_boot.repository.IPaymentProviderRepository;
import com.ra.base_spring_boot.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import com.ra.base_spring_boot.repository.booking.IBookingSeatRepository;

import java.util.*;

@Service
@RequiredArgsConstructor

public class PaymentSelectionService {
    private final EmailService emailService;

    private final IBookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final IPaymentProviderRepository paymentProviderRepository;
    private final IBookingSeatRepository IBookingSeatRepository;
    public ResponseEntity<?> getBookingDetail(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking không tồn tại: " + bookingId));

        List<BookingSeat> seats = IBookingSeatRepository.findByBooking_Id(bookingId);
        Payment payment = paymentRepository.findByBooking_Id(bookingId).orElse(null);

        // ✅ Map booking -> plain data (tránh Hibernate proxy)
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("id", booking.getId());
        bookingData.put("userId", booking.getUser() != null ? booking.getUser().getId() : null);
        bookingData.put("showTimeId", booking.getShowTime() != null ? booking.getShowTime().getId() : null);
        bookingData.put("totalSeat", booking.getTotalSeat());
        bookingData.put("totalPriceMovie", booking.getTotalPriceMovie());
        bookingData.put("createdAt", booking.getCreatedAt());

        // ✅ Map seats -> plain data
        List<Map<String, Object>> seatDataList = new ArrayList<>();
        for (BookingSeat bs : seats) {
            Map<String, Object> s = new HashMap<>();
            s.put("id", bs.getId());
            s.put("seatId", bs.getSeat() != null ? bs.getSeat().getId() : null);

            // SeatName có thể khác tuỳ entity Seat, nên dùng reflection để không lỗi compile
            Object seatObj = bs.getSeat();
            s.put("seatName", seatObj != null ? resolveSeatName(seatObj) : null);

            s.put("quantity", bs.getQuantity());
            seatDataList.add(s);
        }

        // ✅ Map payment -> plain data
        Map<String, Object> paymentData = null;
        if (payment != null) {
            paymentData = new HashMap<>();
            paymentData.put("id", payment.getId());

            // provider (lazy) -> chỉ lấy field đơn giản
            PaymentProvider provider = payment.getProvider();
            paymentData.put("providerId", provider != null ? provider.getId() : null);
            paymentData.put("providerCode", provider != null ? provider.getProviderCode() : null);
            paymentData.put("providerName", provider != null ? provider.getProviderName() : null);

            paymentData.put("paymentMethod", payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : null);
            paymentData.put("paymentStatus", payment.getPaymentStatus() != null ? payment.getPaymentStatus().name() : null);
            paymentData.put("paymentTime", payment.getPaymentTime());
            paymentData.put("amount", payment.getAmount());
            paymentData.put("transactionId", payment.getTransactionId());
        }

        Map<String, Object> data = new HashMap<>();
        data.put("booking", bookingData);
        data.put("seats", seatDataList);
        data.put("payment", paymentData);

        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(data)
                        .build()
        );
    }


    public ResponseEntity<?> choose(Long bookingId, ChoosePaymentSelectionDTO dto) {

        // 1) Validate - chưa chọn thì chặn
        if (dto == null || dto.getProviderId() == null
                || dto.getPaymentMethod() == null || dto.getPaymentMethod().isBlank()) {
            return badRequest("Bạn phải chọn phương thức thanh toán trước khi tiếp tục.");
        }

        // 2) Parse enum paymentMethod
        PaymentMethod method;
        try {
            method = PaymentMethod.valueOf(dto.getPaymentMethod().trim().toUpperCase());
        } catch (Exception e) {
            return badRequest("paymentMethod không hợp lệ: " + dto.getPaymentMethod());
        }

        // 3) Booking tồn tại?
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking không tồn tại: " + bookingId));

        // 4) Provider tồn tại & đang hoạt động?
        PaymentProvider provider = paymentProviderRepository.findById(dto.getProviderId())
                .orElseThrow(() -> new RuntimeException("Provider không tồn tại: " + dto.getProviderId()));

        if (provider.getStatus() == null || !provider.getStatus()) {
            return badRequest("Provider đang bị khóa.");
        }

        // 5) 1 booking chỉ có 1 payment -> tạo mới hoặc update
        Payment payment = paymentRepository.findByBooking_Id(bookingId).orElse(null);

        if (payment == null) {
            payment = Payment.builder()
                    .booking(booking)
                    .provider(provider)                 // ✅ bắt buộc
                    .paymentMethod(method)
                    .paymentStatus(PaymentStatus.PENDING)
                    .amount(booking.getTotalPriceMovie())
                    .build();
        } else {
            payment.setProvider(provider);            // ✅ bắt buộc
            payment.setPaymentMethod(method);
            if (payment.getPaymentStatus() == null) payment.setPaymentStatus(PaymentStatus.PENDING);
            if (payment.getAmount() == null) payment.setAmount(booking.getTotalPriceMovie());
        }

        paymentRepository.save(payment);


        Map<String, Object> data = new HashMap<>();
        data.put("bookingId", bookingId);
        data.put("providerId", provider.getId());
        data.put("paymentMethod", method.name());
        data.put("paymentStatus", payment.getPaymentStatus().name());

        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(data)
                        .build()
        );
    }

    /**
     * Resolve seat name safely without compile-time dependency on a specific getter name.
     */
    private String resolveSeatName(Object seatObj) {
        // Try common getter names
        String[] candidates = {"getSeatName", "getName", "getCode", "getSeatCode", "getLabel"};

        for (String m : candidates) {
            try {
                Object v = seatObj.getClass().getMethod(m).invoke(seatObj);
                if (v != null) return String.valueOf(v);
            } catch (Exception ignored) {
                // try next
            }
        }

        // Fallback: try public fields
        String[] fieldCandidates = {"seatName", "name", "code", "seatCode", "label"};
        for (String f : fieldCandidates) {
            try {
                java.lang.reflect.Field field = seatObj.getClass().getDeclaredField(f);
                field.setAccessible(true);
                Object v = field.get(seatObj);
                if (v != null) return String.valueOf(v);
            } catch (Exception ignored) {
                // try next
            }
        }

        // Last resort
        return seatObj.toString();
    }

    private ResponseEntity<?> badRequest(String msg) {
        Map<String, Object> err = new HashMap<>();
        err.put("error", msg);

        return ResponseEntity.badRequest().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(400)
                        .data(err)
                        .build()
        );
    }
    public ResponseEntity<?> complete(Long bookingId, CompleteBookingDTO dto) {

        Payment payment = paymentRepository.findByBooking_Id(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for booking: " + bookingId));

        // chưa chọn paymentMethod thì không cho complete
        if (payment.getPaymentMethod() == null || payment.getProvider() == null) {
            return badRequest("Bạn phải chọn phương thức thanh toán trước khi hoàn tất.");
        }

        // ✅ Set trạng thái thành công (enum của nhóm có thể không tên SUCCESS)
        PaymentStatus successStatus = resolveSuccessStatus();
        payment.setPaymentStatus(successStatus);
        payment.setPaymentTime(java.time.LocalDateTime.now());

        if (dto != null && dto.getTransactionId() != null && !dto.getTransactionId().isBlank()) {
            payment.setTransactionId(dto.getTransactionId());
        }
        paymentRepository.save(payment);

//          // ===== GỬI MAIL XÁC NHẬN (SAU KHI HOÀN TẤT) =====
//        try {
//            Booking booking = payment.getBooking();
//            if (booking != null && booking.getUser() != null) {
//
//                // Nếu User của cậu không có getEmail() thì đổi sang field đúng:
//                // Ví dụ: getUsername(), getMail(), getGmail()... tùy entity User nhóm cậu.
//                String to = booking.getUser().getEmail();
//
//                if (to != null && !to.isBlank()) {
//                    emailService.sendBookingSuccessMail(
//                            to,
//                            booking.getId(),
//                            booking.getTotalPriceMovie(),
//                            payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : null
//                    );
//                }
//            }
//        } catch (Exception e) {
//            // Không cho crash luồng complete
//            System.out.println("Send mail failed: " + e.getMessage());
//        }

//        paymentRepository.save(payment);
//        // ===== GỬI MAIL XÁC NHẬN (an toàn, không phụ thuộc getEmail) =====
//        try {
//            Booking b = payment.getBooking();
//            Object userObj = (b != null) ? b.getUser() : null;
//            String email = (userObj != null) ? resolveUserEmail(userObj) : null;
//
//            if (email != null && !email.isBlank()) {
//                Long amount = (b != null && b.getTotalPriceMovie() != null) ? b.getTotalPriceMovie().longValue() : null;
//
//                emailService.sendBookingSuccessMail(
//                        email,
//                        (b != null ? b.getId() : bookingId),
//                        amount,
//                        (payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : "")
//                );
//            }
//        } catch (Exception ignored) {
//            // Không để lỗi mail làm crash flow complete
//        }

        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("bookingId", bookingId);
        data.put("paymentStatus", payment.getPaymentStatus().name());
        data.put("paymentTime", payment.getPaymentTime());
        data.put("transactionId", payment.getTransactionId());

        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(data)
                        .build()
        );
    }

    /**
     * Resolve trạng thái "thành công" cho PaymentStatus.
     * Vì enum PaymentStatus của từng nhóm có thể đặt tên khác nhau (SUCCESS/PAID/COMPLETED/DONE...).
     */
    private PaymentStatus resolveSuccessStatus() {
        // Ưu tiên các tên thường gặp
        String[] candidates = {"SUCCESS", "PAID", "COMPLETED", "DONE", "SUCCESSFUL"};

        for (String c : candidates) {
            try {
                return PaymentStatus.valueOf(c);
            } catch (IllegalArgumentException ignored) {
                // tiếp tục thử
            }
        }

        // Fallback: tìm theo keyword trong tên enum
        for (PaymentStatus st : PaymentStatus.values()) {
            String name = st.name().toUpperCase();
            if (name.contains("SUCCESS") || name.contains("PAID") || name.contains("COMPLETE") || name.contains("DONE")) {
                return st;
            }
        }

        // Nếu enum không có trạng thái thành công rõ ràng, dùng giá trị đầu tiên để tránh crash
        return PaymentStatus.values()[0];
    }
    /**
     * Resolve user email safely without compile-time dependency on a specific getter name.
     */
    private String resolveUserEmail(Object userObj) {
        String[] candidates = {"getEmail", "getMail", "getGmail", "getEmailAddress", "getUsername"};

        for (String m : candidates) {
            try {
                Object v = userObj.getClass().getMethod(m).invoke(userObj);
                if (v != null) {
                    String s = String.valueOf(v);
                    if (s.contains("@")) return s; // chỉ nhận nếu giống email
                }
            } catch (Exception ignored) {}
        }

        String[] fieldCandidates = {"email", "mail", "gmail", "emailAddress", "username"};
        for (String f : fieldCandidates) {
            try {
                java.lang.reflect.Field field = userObj.getClass().getDeclaredField(f);
                field.setAccessible(true);
                Object v = field.get(userObj);
                if (v != null) {
                    String s = String.valueOf(v);
                    if (s.contains("@")) return s;
                }
            } catch (Exception ignored) {}
        }

        return null;
    }
}