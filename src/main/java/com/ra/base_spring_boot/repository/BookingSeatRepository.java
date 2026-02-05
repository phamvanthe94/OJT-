package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByMovieResponse;
import com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByScreenResponse;
import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.booking.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
    @Query("""
        SELECT COALESCE(SUM(bs.quantity), 0)
        FROM BookingSeat bs
            JOIN bs.booking b
            , Payment p
        WHERE p.booking.id = b.id
          AND p.paymentStatus = :status
    """)
    Long statisticTotalTicketSold(
            @Param("status") PaymentStatus status
    );
    @Query("""
        SELECT new com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByMovieResponse(
            m.id,
            m.title,
            SUM(bs.quantity)
        )
        FROM BookingSeat bs
            JOIN bs.booking b
            JOIN b.showTime st
            JOIN st.movie m
            JOIN Payment p ON p.booking = b
        WHERE p.paymentStatus = :status
        GROUP BY m.id, m.title
    """)
    List<TicketByMovieResponse> statisticTicketByMovie(PaymentStatus status);

    //  THỐNG KÊ THEO PHÒNG CHIẾU
    @Query("""
        SELECT new com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByScreenResponse(
            s.id,
            s.name,
            SUM(bs.quantity)
        )
        FROM BookingSeat bs
            JOIN bs.booking b
            JOIN b.showTime st
            JOIN st.screen s
            JOIN Payment p ON p.booking = b
        WHERE p.paymentStatus = :status
        GROUP BY s.id, s.name
    """)
    List<TicketByScreenResponse> statisticTicketByScreen(PaymentStatus status);
    List<BookingSeat> findByBooking_Id(Long bookingId);
}