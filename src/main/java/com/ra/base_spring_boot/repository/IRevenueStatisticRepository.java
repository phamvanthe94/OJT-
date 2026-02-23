package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IRevenueStatisticRepository extends JpaRepository<Booking, Long> {

    // Nhóm theo tên phim
    @Query("""
        SELECT m.title,
               SUM(bs.quantity * tp.price)
        FROM BookingSeat bs
            JOIN bs.booking b
            JOIN Payment p ON p.booking = b
            JOIN b.showTime st
            JOIN st.movie m
            JOIN bs.ticketPrice tp
        WHERE p.paymentStatus = :status
          AND p.paymentTime BETWEEN :from AND :to
        GROUP BY m.title
        ORDER BY m.title
    """)
    List<Object[]> revenueByMovie(
            @Param("status") PaymentStatus status,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );



    // Nhóm theo thể loại phim
    @Query("""
    SELECT g.genreName, SUM(b.totalPriceMovie)
    FROM Booking b
    JOIN b.showTime st
    JOIN st.movie m
    JOIN m.genres g
    WHERE b.createdAt BETWEEN :from AND :to
    GROUP BY g.genreName
""")
    List<Object[]> revenueByGenre(LocalDateTime from, LocalDateTime to);


    // Nhóm theo ngày
    @Query("""
        SELECT DATE(b.createdAt), SUM(b.totalPriceMovie)
        FROM Booking b
        WHERE b.createdAt BETWEEN :from AND :to
        GROUP BY DATE(b.createdAt)
        ORDER BY DATE(b.createdAt)
    """)
    List<Object[]> revenueByDate(LocalDateTime from, LocalDateTime to);

    // Nhóm theo tháng
    @Query("""
        SELECT FUNCTION('MONTH', b.createdAt), SUM(b.totalPriceMovie)
        FROM Booking b
        WHERE b.createdAt BETWEEN :from AND :to
        GROUP BY FUNCTION('MONTH', b.createdAt)
    """)
    List<Object[]> revenueByMonth(LocalDateTime from, LocalDateTime to);

    // Nhóm theo năm
    @Query("""
        SELECT FUNCTION('YEAR', b.createdAt), SUM(b.totalPriceMovie)
        FROM Booking b
        WHERE b.createdAt BETWEEN :from AND :to
        GROUP BY FUNCTION('YEAR', b.createdAt)
    """)
    List<Object[]> revenueByYear(LocalDateTime from, LocalDateTime to);
}
