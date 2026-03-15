package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.resp.ChangePaymentStatusDTO;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.booking.Booking;
import com.ra.base_spring_boot.model.entity.booking.BookingSeat;
import com.ra.base_spring_boot.model.entity.booking.Payment;
import com.ra.base_spring_boot.model.entity.booking.PaymentProvider;
import com.ra.base_spring_boot.repository.booking.IBookingRepository;
import com.ra.base_spring_boot.repository.booking.IBookingSeatRepository;
import com.ra.base_spring_boot.repository.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingAdminService {

    private final IBookingRepository bookingRepository;
    private final IBookingSeatRepository IBookingSeatRepository;
    private final PaymentRepository paymentRepository;

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

    public ResponseEntity<ResponseWrapper<Map<String, Object>>> getDetail(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new HttpNotFound("Booking not found with id: " + bookingId));

        List<BookingSeat> seats = IBookingSeatRepository.findByBooking_Id(bookingId);
        Payment payment = paymentRepository.findByBooking_Id(bookingId).orElse(null);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("booking", bookingToMap(booking));
        result.put("seats", seats.stream().map(this::bookingSeatToMap).toList());
        result.put("payment", payment == null ? null : paymentToMap(payment));

        return ResponseEntity.ok(
                ResponseWrapper.<Map<String, Object>>builder()
                        .code(200)
                        .status(HttpStatus.OK)
                        .data(result)
                        .build()
        );
    }

    public ResponseEntity<ResponseWrapper<Object>> changePaymentStatus(Long bookingId, ChangePaymentStatusDTO dto) {

        if (dto == null || dto.getNewStatus() == null || dto.getNewStatus().isBlank()) {

            Map<String, Object> err = new LinkedHashMap<>();
            err.put("error", "newStatus cannot be blank");

            return ResponseEntity.badRequest().body(
                    ResponseWrapper.builder()
                            .code(400)
                            .status(HttpStatus.BAD_REQUEST)
                            .data(err)
                            .build()
            );
        }

        Payment payment = paymentRepository.findByBooking_Id(bookingId)
                .orElse(null);

        if (payment == null) {
            Map<String, Object> err = new LinkedHashMap<>();
            err.put("error", "Payment not found for bookingId: " + bookingId);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseWrapper.builder()
                            .code(404)
                            .status(HttpStatus.NOT_FOUND)
                            .data(err)
                            .build()
            );
        }

        PaymentStatus statusEnum;
        try {
            statusEnum = PaymentStatus.valueOf(dto.getNewStatus().trim().toUpperCase());
        } catch (Exception e) {

            Map<String, Object> err = new LinkedHashMap<>();
            err.put("error", "Invalid PaymentStatus: " + dto.getNewStatus());

            return ResponseEntity.badRequest().body(
                    ResponseWrapper.builder()
                            .code(400)
                            .status(HttpStatus.BAD_REQUEST)
                            .data(err)
                            .build()
            );
        }

        payment.setPaymentStatus(statusEnum);
        paymentRepository.save(payment);

        Map<String, Object> ok = new LinkedHashMap<>();
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

    private Map<String, Object> bookingToMap(Booking booking) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", booking.getId());
        data.put("bookingCode", booking.getBookingCode());
        data.put("totalSeat", booking.getTotalSeat());
        data.put("totalAmount", booking.getTotalAmount());
        data.put("status", booking.getStatus());
        data.put("paymentStatus", booking.getPaymentStatus());
        data.put("createdAt", booking.getCreatedAt());
        data.put("updatedAt", booking.getUpdateAt());
        return data;
    }

    private Map<String, Object> bookingSeatToMap(BookingSeat bookingSeat) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", bookingSeat.getId());
        data.put("seatId", bookingSeat.getSeat() == null ? null : bookingSeat.getSeat().getId());
        data.put("seatNumber", bookingSeat.getSeat() == null ? null : bookingSeat.getSeat().getSeatNumber());
        data.put("seatType", bookingSeat.getSeat() == null ? null : bookingSeat.getSeat().getType());
        data.put("quantity", bookingSeat.getQuantity());
        data.put("price", bookingSeat.getPrice());
        data.put("createdAt", bookingSeat.getCreatedAt());
        return data;
    }

    private Map<String, Object> paymentToMap(Payment payment) {
        PaymentProvider provider = payment.getProvider();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", payment.getId());
        data.put("paymentMethod", payment.getPaymentMethod());
        data.put("paymentStatus", payment.getPaymentStatus());
        data.put("paymentTime", payment.getPaymentTime());
        data.put("amount", payment.getAmount());
        data.put("transactionId", payment.getTransactionId());
        data.put("paypalOrderId", payment.getPaypalOrderId());
        data.put("paypalCaptureId", payment.getPaypalCaptureId());
        data.put("provider", provider == null ? null : paymentProviderToMap(provider));
        return data;
    }

    private Map<String, Object> paymentProviderToMap(PaymentProvider provider) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", provider.getId());
        data.put("providerName", provider.getProviderName());
        data.put("providerCode", provider.getProviderCode());
        data.put("status", provider.getStatus());
        return data;
    }
}
