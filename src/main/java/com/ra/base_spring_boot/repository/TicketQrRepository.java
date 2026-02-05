package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.dto.resp.TicketQrData;
import com.ra.base_spring_boot.model.entity.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketQrRepository extends JpaRepository<Booking, Long> {
    @Query("""
                SELECT new com.ra.base_spring_boot.dto.resp.TicketQrData(
                    b.id,
                    u.firstName,
                    m.title,
                    sc.name,
                    st.startTime,
                    s.seatNumber
                )
                FROM BookingSeat bs
                    JOIN bs.booking b
                    JOIN b.user u
                    JOIN b.showTime st
                    JOIN st.movie m
                    JOIN st.screen sc
                    JOIN bs.seat s
                    JOIN Payment p ON p.booking = b
                WHERE b.id = :bookingId
                  AND p.paymentStatus = com.ra.base_spring_boot.model.constants.PaymentStatus.COMPLETED
            """)
    List<TicketQrData> getQrDataByBookingId(Long bookingId);
}
