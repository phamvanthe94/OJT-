package com.ra.base_spring_boot.services.booking;

import com.ra.base_spring_boot.dto.req.BookingRequest;
import com.ra.base_spring_boot.dto.resp.BookingResponse;

public interface IBookingService {

    BookingResponse createBooking(BookingRequest request);

    BookingResponse completePayment(String bookingCode);

    BookingResponse getBookingById(Long id);

    BookingResponse getBookingByCode(String bookingCode);
}
