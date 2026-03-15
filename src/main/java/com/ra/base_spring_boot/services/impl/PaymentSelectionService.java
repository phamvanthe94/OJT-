package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.ChoosePaymentSelectionDTO;
import com.ra.base_spring_boot.dto.req.CompleteBookingDTO;
import com.ra.base_spring_boot.model.constants.BookingStatus;
import com.ra.base_spring_boot.model.constants.PaymentMethod;
import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.booking.Booking;
import com.ra.base_spring_boot.model.entity.booking.BookingSeat;
import com.ra.base_spring_boot.model.entity.booking.Payment;
import com.ra.base_spring_boot.model.entity.booking.PaymentProvider;
import com.ra.base_spring_boot.repository.booking.IBookingRepository;
import com.ra.base_spring_boot.repository.booking.IBookingSeatRepository;
import com.ra.base_spring_boot.repository.payment.IPaymentProviderRepository;
import com.ra.base_spring_boot.repository.payment.PaymentRepository;
import com.ra.base_spring_boot.services.paymt.impl.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentSelectionService {


    private final EmailServiceImpl emailService;

    private final IBookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final IPaymentProviderRepository paymentProviderRepository;
    private final IBookingSeatRepository bookingSeatRepository;

    public ResponseEntity<?> getBookingDetail(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        List<BookingSeat> seats = bookingSeatRepository.findByBooking_Id(bookingId);
        Payment payment = paymentRepository.findByBooking_Id(bookingId).orElse(null);

        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("id", booking.getId());
        bookingData.put("userId", booking.getUser() != null ? booking.getUser().getId() : null);
        bookingData.put("showTimeId", booking.getShowTime() != null ? booking.getShowTime().getId() : null);
        bookingData.put("totalSeat", booking.getTotalSeat());
        bookingData.put("totalAmount", booking.getTotalAmount());
        bookingData.put("createdAt", booking.getCreatedAt());

        List<Map<String, Object>> seatDataList = new ArrayList<>();
        for (BookingSeat bs : seats) {
            Map<String, Object> s = new HashMap<>();
            s.put("id", bs.getId());
            s.put("seatId", bs.getSeat() != null ? bs.getSeat().getId() : null);

            Object seatObj = bs.getSeat();
            s.put("seatName", seatObj != null ? resolveSeatName(seatObj) : null);

            s.put("quantity", bs.getQuantity());
            seatDataList.add(s);
        }

        Map<String, Object> paymentData = null;
        if (payment != null) {
            paymentData = new HashMap<>();
            paymentData.put("id", payment.getId());

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

        if (dto == null || dto.getProviderId() == null
                || dto.getPaymentMethod() == null || dto.getPaymentMethod().isBlank()) {
            return badRequest("Provider is disabled.");
        }

        PaymentMethod method;
        try {
            method = PaymentMethod.valueOf(dto.getPaymentMethod().trim().toUpperCase());
        } catch (Exception e) {
            return badRequest("Invalid paymentMethod: " + dto.getPaymentMethod());
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        PaymentProvider provider = paymentProviderRepository.findById(dto.getProviderId())
                .orElseThrow(() -> new RuntimeException("Provider not found: " + dto.getProviderId()));

        if (provider.getStatus() == null || !provider.getStatus()) {
            return badRequest("Provider is disabled.");
        }

        Payment payment = paymentRepository.findByBooking_Id(bookingId).orElse(null);

        BigDecimal amount = toMoney(booking.getTotalAmount());

        if (payment == null) {
            payment = Payment.builder()
                    .booking(booking)
                    .provider(provider)
                    .paymentMethod(method)
                    .paymentStatus(PaymentStatus.PENDING)
                    .amount(amount)
                    .build();
        } else {
            payment.setProvider(provider);
            payment.setPaymentMethod(method);
            if (payment.getPaymentStatus() == null) payment.setPaymentStatus(PaymentStatus.PENDING);
            if (payment.getAmount() == null) payment.setAmount(amount);
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

    public ResponseEntity<?> complete(Long bookingId, CompleteBookingDTO dto) {

        Payment payment = paymentRepository.findByBooking_Id(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for booking: " + bookingId));

        if (payment.getPaymentMethod() == null || payment.getProvider() == null) {
            return badRequest("Provider ID and payment method are required before completing booking.");
        }

        PaymentStatus successStatus = resolveSuccessStatus();
        payment.setPaymentStatus(successStatus);
        payment.setPaymentTime(LocalDateTime.now());

        Booking booking = payment.getBooking();
        if (booking != null) {
            booking.setPaymentStatus(successStatus);
            booking.setStatus(BookingStatus.COMPLETED);
            bookingRepository.save(booking);
        }

        if (dto != null && dto.getTransactionId() != null && !dto.getTransactionId().isBlank()) {
            payment.setTransactionId(dto.getTransactionId());
        }

        paymentRepository.save(payment);

        Map<String, Object> data = new HashMap<>();
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


    private BigDecimal toMoney(Double value) {
        return value == null ? BigDecimal.ZERO : BigDecimal.valueOf(value);
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

    private String resolveSeatName(Object seatObj) {
        String[] candidates = {"getSeatName", "getName", "getCode", "getSeatCode", "getLabel"};
        for (String m : candidates) {
            try {
                Object v = seatObj.getClass().getMethod(m).invoke(seatObj);
                if (v != null) return String.valueOf(v);
            } catch (Exception ignored) {
            }
        }

        String[] fieldCandidates = {"seatName", "name", "code", "seatCode", "label"};
        for (String f : fieldCandidates) {
            try {
                java.lang.reflect.Field field = seatObj.getClass().getDeclaredField(f);
                field.setAccessible(true);
                Object v = field.get(seatObj);
                if (v != null) return String.valueOf(v);
            } catch (Exception ignored) {
            }
        }

        return seatObj.toString();
    }

    private PaymentStatus resolveSuccessStatus() {
        String[] candidates = {"SUCCESS", "PAID", "COMPLETED", "DONE", "SUCCESSFUL"};

        for (String c : candidates) {
            try {
                return PaymentStatus.valueOf(c);
            } catch (IllegalArgumentException ignored) {
            }
        }

        for (PaymentStatus st : PaymentStatus.values()) {
            String name = st.name().toUpperCase();
            if (name.contains("SUCCESS") || name.contains("PAID") || name.contains("COMPLETE") || name.contains("DONE")) {
                return st;
            }
        }

        return PaymentStatus.values()[0];
    }
}

