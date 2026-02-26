package com.ra.base_spring_boot.repository.payment;

import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.booking.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Lấy payment theo bookingId (nếu bạn vẫn muốn dùng)
    Optional<Payment> findByBooking_Id(Long bookingId);

    // Map return/webhook theo PayPal orderId
    Optional<Payment> findByPaypalOrderId(String paypalOrderId);

    // ✅ Dùng để chặn thanh toán lại nếu booking đã có payment COMPLETED
    boolean existsByBooking_IdAndPaymentStatus(Long bookingId, PaymentStatus paymentStatus);

    // ✅ Dùng để chặn spam click (đang có PENDING)
    boolean existsByBooking_IdAndPaymentStatusIn(Long bookingId, Collection<PaymentStatus> statuses);
}