package com.ra.base_spring_boot.repository.payment;

import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.booking.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByBooking_Id(Long bookingId);

    Optional<Payment> findByPaypalOrderId(String paypalOrderId);

    boolean existsByBooking_IdAndPaymentStatus(Long bookingId, PaymentStatus paymentStatus);

    boolean existsByBooking_IdAndPaymentStatusIn(Long bookingId, Collection<PaymentStatus> statuses);
}
