package com.ra.base_spring_boot.repository.booking;

import com.ra.base_spring_boot.model.entity.theater.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISeatRepository extends JpaRepository<Seat,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id = :id")
    Optional<Seat> findByIdForUpdate(@Param("id") Long id);

    @Query(value = """
        SELECT 
            s.id AS seatId,
            s.seat_number AS seatNumber,
            s.type AS type,
            CASE 
                WHEN EXISTS (
                    SELECT 1
                    FROM booking_seat bs
                    JOIN bookings b ON b.id = bs.booking_id
                    WHERE bs.seat_id = s.id
                      AND b.showtime_id = :showTimeId
                      AND b.payment_status IN ('PENDING', 'COMPLETED')
                ) THEN true
                ELSE false
            END AS booked
        FROM seats s
        WHERE s.screen_id = :screenId
        ORDER BY s.seat_number
    """, nativeQuery = true)
    List<Object[]> findSeatsWithBookingStatus(
            @Param("screenId") Long screenId,
            @Param("showTimeId") Long showTimeId
    );
    @Query("""
       SELECT COALESCE(SUM(bs.price),0)
       FROM BookingSeat bs
       WHERE bs.booking.paymentStatus = com.ra.base_spring_boot.model.constants.PaymentStatus.COMPLETED
       """)
    Double getTotalRevenue();
}
