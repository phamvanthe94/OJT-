package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.entity.booking.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBooking_Id(Long bookingId);
}