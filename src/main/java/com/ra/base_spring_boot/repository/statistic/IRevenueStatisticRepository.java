package com.ra.base_spring_boot.repository.statistic;

import com.ra.base_spring_boot.dto.statistic.resp.RevenueStatisticResponse;
import com.ra.base_spring_boot.model.constants.BookingStatus;
import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IRevenueStatisticRepository extends JpaRepository<Booking, Long> {

    // Thống kê theo phim
    @Query("""
                SELECT NEW com.ra.base_spring_boot.dto.statistic.resp.RevenueStatisticResponse(
                    m.id, m.title,
                    SUM(CASE WHEN bs.booking.createdAt BETWEEN :startToday AND :endToday THEN bs.price ELSE 0.0 END),
                    SUM(CASE WHEN bs.booking.createdAt BETWEEN :startWeek AND :now THEN bs.price ELSE 0.0 END),
                    SUM(CASE WHEN bs.booking.createdAt BETWEEN :startMonth AND :now THEN bs.price ELSE 0.0 END),
                    SUM(CASE WHEN bs.booking.createdAt BETWEEN :startYear AND :now THEN bs.price ELSE 0.0 END),
                    SUM(bs.price)
                )
                FROM BookingSeat bs
                JOIN bs.booking.showTime st
                JOIN st.movie m
                WHERE bs.booking.status = :status
                AND (:movieId IS NULL OR m.id = :movieId)
                GROUP BY m.id, m.title
            """)
    List<RevenueStatisticResponse> revenueByMovie(
            @Param("status") BookingStatus status,
            @Param("movieId") Long movieId,
            @Param("startToday") LocalDateTime startToday,
            @Param("endToday") LocalDateTime endToday,
            @Param("startWeek") LocalDateTime startWeek,
            @Param("startMonth") LocalDateTime startMonth,
            @Param("startYear") LocalDateTime startYear,
            @Param("now") LocalDateTime now
    );

    // Thống kê theo thể loại (join nhiều-nhiều)
    @Query("""
                SELECT NEW com.ra.base_spring_boot.dto.statistic.resp.RevenueStatisticResponse(
                    g.id, g.genreName,
                    SUM(CASE WHEN bs.booking.createdAt BETWEEN :startToday AND :endToday THEN bs.price ELSE 0.0 END),
                    SUM(CASE WHEN bs.booking.createdAt BETWEEN :startWeek AND :now THEN bs.price ELSE 0.0 END),
                    SUM(CASE WHEN bs.booking.createdAt BETWEEN :startMonth AND :now THEN bs.price ELSE 0.0 END),
                    SUM(CASE WHEN bs.booking.createdAt BETWEEN :startYear AND :now THEN bs.price ELSE 0.0 END),
                    SUM(bs.price)
                )
                FROM BookingSeat bs
                JOIN bs.booking.showTime st
                JOIN st.movie m
                JOIN m.genres g
                WHERE bs.booking.status = :status
                AND (:genreId IS NULL OR g.id = :genreId)
                GROUP BY g.id, g.genreName
            """)
    List<RevenueStatisticResponse> revenueByGenre(
            @Param("status") BookingStatus status,
            @Param("genreId") Long genreId,
            @Param("startToday") LocalDateTime startToday,
            @Param("endToday") LocalDateTime endToday,
            @Param("startWeek") LocalDateTime startWeek,
            @Param("startMonth") LocalDateTime startMonth,
            @Param("startYear") LocalDateTime startYear,
            @Param("now") LocalDateTime now
    );

    // Thống kê theo rạp
    @Query("""
                SELECT NEW com.ra.base_spring_boot.dto.statistic.resp.RevenueStatisticResponse(
                    t.id, t.name,
                    SUM(CASE WHEN bs.booking.createdAt BETWEEN :startToday AND :endToday THEN bs.price ELSE 0.0 END),
                    SUM(CASE WHEN bs.booking.createdAt BETWEEN :startWeek AND :now THEN bs.price ELSE 0.0 END),
                    SUM(CASE WHEN bs.booking.createdAt BETWEEN :startMonth AND :now THEN bs.price ELSE 0.0 END),
                    SUM(CASE WHEN bs.booking.createdAt BETWEEN :startYear AND :now THEN bs.price ELSE 0.0 END),
                    SUM(bs.price)
                )
                FROM BookingSeat bs
                JOIN bs.booking.showTime st
                JOIN st.screen s
                JOIN s.theater t
                WHERE bs.booking.status = :status
                AND (:theaterId IS NULL OR t.id = :theaterId)
                GROUP BY t.id, t.name
            """)
    List<RevenueStatisticResponse> revenueByTheater(
            @Param("status") BookingStatus status,
            @Param("theaterId") Long theaterId,
            @Param("startToday") LocalDateTime startToday,
            @Param("endToday") LocalDateTime endToday,
            @Param("startWeek") LocalDateTime startWeek,
            @Param("startMonth") LocalDateTime startMonth,
            @Param("startYear") LocalDateTime startYear,
            @Param("now") LocalDateTime now
    );

    // Thống kê theo phòng chiếu
    @Query("""
                SELECT NEW com.ra.base_spring_boot.dto.statistic.resp.RevenueStatisticResponse(
                    s.id, s.name,
                    SUM(CASE WHEN bs.booking.createdAt BETWEEN :startToday AND :endToday THEN bs.price ELSE 0.0 END),
                    SUM(CASE WHEN bs.booking.createdAt BETWEEN :startWeek AND :now THEN bs.price ELSE 0.0 END),
                    SUM(CASE WHEN bs.booking.createdAt BETWEEN :startMonth AND :now THEN bs.price ELSE 0.0 END),
                    SUM(CASE WHEN bs.booking.createdAt BETWEEN :startYear AND :now THEN bs.price ELSE 0.0 END),
                    SUM(bs.price)
                )
                FROM BookingSeat bs
                JOIN bs.booking.showTime st
                JOIN st.screen s
                WHERE bs.booking.status = :status
                AND (:screenId IS NULL OR s.id = :screenId)
                GROUP BY s.id, s.name
            """)
    List<RevenueStatisticResponse> revenueByScreen(
            @Param("status") BookingStatus status,
            @Param("screenId") Long screenId,
            @Param("startToday") LocalDateTime startToday,
            @Param("endToday") LocalDateTime endToday,
            @Param("startWeek") LocalDateTime startWeek,
            @Param("startMonth") LocalDateTime startMonth,
            @Param("startYear") LocalDateTime startYear,
            @Param("now") LocalDateTime now
    );

    // Export excel: doanh thu theo phim theo khoảng thời gian thanh toán
    @Query("""
                SELECT m.title,
                       COALESCE(SUM(bs.quantity * bs.price), 0)
                FROM BookingSeat bs
                    JOIN bs.booking b
                    JOIN Payment p ON p.booking = b
                    JOIN b.showTime st
                    JOIN st.movie m
                WHERE p.paymentStatus = :status
                  AND p.paymentTime BETWEEN :from AND :to
                GROUP BY m.title
                ORDER BY m.title
            """)
    List<Object[]> revenueByMovieExportExcel(
            @Param("status") PaymentStatus status,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}