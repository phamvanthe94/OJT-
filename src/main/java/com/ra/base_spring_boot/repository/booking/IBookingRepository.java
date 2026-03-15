package com.ra.base_spring_boot.repository.booking;

import com.ra.base_spring_boot.model.constants.BookingStatus;
import com.ra.base_spring_boot.model.entity.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IBookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingCode(String bookingCode);
}
