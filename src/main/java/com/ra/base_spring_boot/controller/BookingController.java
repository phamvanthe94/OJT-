package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.req.BookingRequest;
import com.ra.base_spring_boot.dto.resp.BookingResponse;
import com.ra.base_spring_boot.services.booking.IBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final IBookingService bookingService;

    @PostMapping
    public BookingResponse createBooking(
            @Valid @RequestBody BookingRequest request
    ) {
        return bookingService.createBooking(request);
    }

    @GetMapping("/detail/{id}")
    public BookingResponse getBookingById(
            @PathVariable Long id
    ) {
        return bookingService.getBookingById(id);
    }

    @GetMapping("/code/{bookingCode}")
    public BookingResponse getBookingByCode(
            @PathVariable String bookingCode
    ) {
        return bookingService.getBookingByCode(bookingCode);
    }

    @PutMapping("/complete/{bookingCode}")
    public BookingResponse complete(@PathVariable String bookingCode) {
        return bookingService.completePayment(bookingCode);
    }


}
