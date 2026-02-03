package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.entity.theater.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat,Long> {
    @Query(value = """
        SELECT 
            s.id AS seatId,
            s.seat_number AS seatNumber,
            s.type AS type,
            CASE 
                WHEN bs.id IS NULL THEN false
                ELSE true
            END AS booked
        FROM seats s
        LEFT JOIN booking_seat bs ON bs.seat_id = s.id
        LEFT JOIN bookings b ON b.id = bs.booking_id
            AND b.showtime_id = :showTimeId
        WHERE s.screen_id = :screenId
        ORDER BY s.seat_number
    """, nativeQuery = true)
    List<Object[]> findSeatsWithBookingStatus(
            @Param("screenId") Long screenId,
            @Param("showTimeId") Long showTimeId
    );
}
