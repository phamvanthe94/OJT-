package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.req.ChoosePaymentSelectionDTO;
import com.ra.base_spring_boot.services.PaymentSelectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class PaymentSelectionController {

    private final PaymentSelectionService paymentSelectionService;
    // ✅ Xem lại đơn đặt vé (booking + seats + payment)

    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBookingDetail(@PathVariable Long bookingId) {
        return paymentSelectionService.getBookingDetail(bookingId);
    }

    @PutMapping("/{bookingId}/payment-selection")
    public ResponseEntity<?> choose(@PathVariable Long bookingId,
                                    @RequestBody ChoosePaymentSelectionDTO dto) {
        return paymentSelectionService.choose(bookingId, dto);
    }
    @PostMapping("/{bookingId}/complete")
    public ResponseEntity<?> complete(@PathVariable Long bookingId,
                                      @RequestBody(required = false) com.ra.base_spring_boot.dto.req.CompleteBookingDTO dto) {
        return paymentSelectionService.complete(bookingId, dto);
    }
}