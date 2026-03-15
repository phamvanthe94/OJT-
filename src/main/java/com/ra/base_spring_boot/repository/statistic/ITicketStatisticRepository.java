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
    Long statisticTotalTicketSold(@Param("status") PaymentStatus status);

    @Query("""
                SELECT new com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByMovieResponse(
                    m.id,
                    m.title,
                    COALESCE(SUM(bs.quantity), 0)
                )
                FROM BookingSeat bs
                    JOIN bs.booking b
                    JOIN b.showTime st
                    JOIN st.movie m
                    JOIN Payment p ON p.booking = b
                WHERE p.paymentStatus = :status
                GROUP BY m.id, m.title
            """)
    List<TicketByMovieResponse> statisticTicketByMovie(@Param("status") PaymentStatus status);

    @Query("""
                SELECT new com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByScreenResponse(
                    s.id,
                    s.name,
                    COALESCE(SUM(bs.quantity), 0)
                )
                FROM BookingSeat bs
                    JOIN bs.booking b
                    JOIN b.showTime st
                    JOIN st.screen s
                    JOIN Payment p ON p.booking = b
                WHERE p.paymentStatus = :status
                GROUP BY s.id, s.name
            """)
    List<TicketByScreenResponse> statisticTicketByScreen(@Param("status") PaymentStatus status);

    @Query("""
                SELECT
                    m.title,
                    s.type,
                    bs.price,
                    COALESCE(SUM(bs.quantity), 0),
                    COALESCE(SUM(bs.quantity * bs.price), 0)
                FROM BookingSeat bs
                    JOIN bs.booking b
                    JOIN b.showTime st
                    JOIN st.movie m
                    JOIN bs.seat s
                WHERE b.createdAt IS NOT NULL
                GROUP BY m.title, s.type, bs.price
            """)
    List<Object[]> statisticTicketAll();

    @Query("""
                SELECT
                    m.title,
                    s.type,
                    bs.price,
                    COALESCE(SUM(bs.quantity), 0),
                    COALESCE(SUM(bs.quantity * bs.price), 0)
                FROM BookingSeat bs
                    JOIN bs.booking b
                    JOIN b.showTime st
                    JOIN st.movie m
                    JOIN bs.seat s
                    JOIN Payment p ON p.booking = b
                WHERE p.paymentStatus = :status
                  AND p.paymentTime BETWEEN :from AND :to
                GROUP BY m.title, s.type, bs.price
            """)
    List<Object[]> statisticTicket(
            @Param("status") PaymentStatus status,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query("""
                SELECT
                    m.title AS movieTitle,
                    s.type AS seatType,
                    bs.price AS price,
                    COALESCE(SUM(bs.quantity), 0) AS quantity,
                    COALESCE(SUM(bs.quantity * bs.price), 0) AS totalAmount
                FROM BookingSeat bs
                    JOIN bs.booking b
                    JOIN Payment p ON p.booking = b
                    JOIN b.showTime st
                    JOIN st.movie m
                    JOIN bs.seat s
                WHERE p.paymentStatus = :status
                  AND p.paymentTime BETWEEN :from AND :to
                GROUP BY m.title, s.type, bs.price
            """)
    List<TicketStatisticExcelView> statisticTicketForExcel(
            @Param("status") PaymentStatus status,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
