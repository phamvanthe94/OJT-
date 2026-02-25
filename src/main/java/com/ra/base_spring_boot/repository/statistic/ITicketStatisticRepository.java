package com.ra.base_spring_boot.repository.statistic;

import com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByMovieResponse;
import com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByScreenResponse;
import com.ra.base_spring_boot.dto.resp.statisticResponse.TicketStatisticExcelView;
import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.booking.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ITicketStatisticRepository extends JpaRepository<BookingSeat, Long> {
    @Query("""
                SELECT COALESCE(SUM(bs.quantity), 0)
                FROM BookingSeat bs
                    JOIN bs.booking b
                    JOIN Payment p ON p.booking = b
                WHERE p.paymentStatus = :status
            """)
    Long statisticTotalTicketSold(
            @Param("status") PaymentStatus status
    );

    // THỐNG KÊ THEO PHIM
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
    List<TicketByMovieResponse> statisticTicketByMovie(
            @Param("status") PaymentStatus status
    );

    // THỐNG KÊ THEO PHÒNG CHIẾU
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
    List<TicketByScreenResponse> statisticTicketByScreen(
            @Param("status") PaymentStatus status
    );

    // Method test - không có điều kiện để xem có dữ liệu không
    @Query("""
    SELECT 
        m.title,
        tp.typeSeat,
        tp.price,
        SUM(bs.quantity),
        SUM(bs.quantity * tp.price)
    FROM BookingSeat bs
    JOIN bs.booking b
    JOIN b.showTime st
    JOIN st.movie m
    JOIN bs.ticketPrice tp
    WHERE b.createdAt IS NOT NULL
    GROUP BY m.title, tp.typeSeat, tp.price
    """)
    List<Object[]> statisticTicketAll();

    // Query chính với điều kiện
    @Query("""
    SELECT 
        m.title,
        tp.typeSeat,
        tp.price,
        SUM(bs.quantity),
        SUM(bs.quantity * tp.price)
    FROM BookingSeat bs
    JOIN bs.booking b
    JOIN b.showTime st
    JOIN st.movie m
    JOIN bs.ticketPrice tp
    JOIN Payment p ON p.booking = b
    WHERE p.paymentStatus = :status
      AND b.createdAt BETWEEN :from AND :to
    GROUP BY m.title, tp.typeSeat, tp.price
    """)
    List<Object[]> statisticTicket(
            @Param("status") PaymentStatus status,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
    @Query("""
    SELECT
        m.title AS movieTitle,
        tp.typeSeat AS seatType,
        tp.price AS price,
        SUM(bs.quantity) AS quantity,
        SUM(bs.quantity * tp.price * 1.0) AS totalAmount
    FROM BookingSeat bs
        JOIN bs.booking b
        JOIN Payment p ON p.booking = b
        JOIN b.showTime st
        JOIN st.movie m
        JOIN bs.ticketPrice tp
    WHERE p.paymentStatus = :status
      AND p.paymentTime BETWEEN :from AND :to
    GROUP BY m.title, tp.typeSeat, tp.price
""")
    List<TicketStatisticExcelView> statisticTicketForExcel(
            @Param("status") PaymentStatus status,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );


}
