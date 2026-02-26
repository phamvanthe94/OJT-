package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.resp.ChangePaymentStatusDTO;
import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.booking.Booking;
import com.ra.base_spring_boot.model.entity.booking.BookingSeat;
import com.ra.base_spring_boot.model.entity.booking.Payment;
import com.ra.base_spring_boot.repository.booking.IBookingRepository;
import com.ra.base_spring_boot.repository.booking.IBookingSeatRepository;
import com.ra.base_spring_boot.repository.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingAdminService {

    private final IBookingRepository bookingRepository;
    private final IBookingSeatRepository IBookingSeatRepository;
    private final PaymentRepository paymentRepository;

    // ✅ 1) LIST + SEARCH + PAGING
    public ResponseEntity<ResponseWrapper<Page<Booking>>> getAll(String keyword, Pageable pageable) {

        Page<Booking> page = bookingRepository.findAll(pageable);

        return ResponseEntity.ok(
                ResponseWrapper.<Page<Booking>>builder()
                        .code(200)
                        .status(HttpStatus.OK)
                        .data(page)
                        .build()
        );
    }

    // ✅ 2) DETAIL: booking + seats + payment
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> getDetail(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        List<BookingSeat> seats = IBookingSeatRepository.findByBooking_Id(bookingId);
        Payment payment = paymentRepository.findByBooking_Id(bookingId).orElse(null);

        Map<String, Object> result = new HashMap<>();
        result.put("booking", booking);
        result.put("seats", seats);
        result.put("payment", payment);

        return ResponseEntity.ok(
                ResponseWrapper.<Map<String, Object>>builder()
                        .code(200)
                        .status(HttpStatus.OK)
                        .data(result)
                        .build()
        );
    }

    // ✅ 3) CHANGE STATUS: đổi PaymentStatus theo bookingId
    public ResponseEntity<ResponseWrapper<Object>> changePaymentStatus(Long bookingId, ChangePaymentStatusDTO dto) {

        // 1) validate newStatus
        if (dto == null || dto.getNewStatus() == null || dto.getNewStatus().isBlank()) {

            Map<String, Object> err = new HashMap<>();
            err.put("error", "newStatus cannot be blank");

            return ResponseEntity.badRequest().body(
                    ResponseWrapper.builder()
                            .code(400)
                            .status(HttpStatus.BAD_REQUEST)
                            .data(err)
                            .build()
            );
        }

        // 2) find payment
        Payment payment = paymentRepository.findByBooking_Id(bookingId)
                .orElse(null);

        if (payment == null) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "Payment not found for bookingId: " + bookingId);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseWrapper.builder()
                            .code(404)
                            .status(HttpStatus.NOT_FOUND)
                            .data(err)
                            .build()
            );
        }

        // 3) parse enum
        PaymentStatus statusEnum;
        try {
            statusEnum = PaymentStatus.valueOf(dto.getNewStatus().trim().toUpperCase());
        } catch (Exception e) {

            Map<String, Object> err = new HashMap<>();
            err.put("error", "Invalid PaymentStatus: " + dto.getNewStatus());

            return ResponseEntity.badRequest().body(
                    ResponseWrapper.builder()
                            .code(400)
                            .status(HttpStatus.BAD_REQUEST)
                            .data(err)
                            .build()
            );
        }

        // 4) update + save
        payment.setPaymentStatus(statusEnum);
        paymentRepository.save(payment);

        Map<String, Object> ok = new HashMap<>();
        ok.put("bookingId", bookingId);
        ok.put("paymentStatus", statusEnum.name());

        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .code(200)
                        .status(HttpStatus.OK)
                        .data(ok)
                        .build()
        );
    }
}