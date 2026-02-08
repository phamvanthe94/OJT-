package com.ra.base_spring_boot.repository.Statistic;

import com.ra.base_spring_boot.model.entity.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface IMovieStatisticRepository extends JpaRepository<Movie, Long> {

    /**
     * Đã chiếu: releaseDate < today
     */
    @Query("""
        SELECT COUNT(m)
        FROM Movie m
        WHERE m.releaseDate < CURRENT_DATE
          AND (:fromDate IS NULL OR m.releaseDate >= :fromDate)
          AND (:toDate IS NULL OR m.releaseDate <= :toDate)
    """)
    long countReleased(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    /**
     * Đang chiếu: releaseDate = today
     */
    @Query("""
        SELECT COUNT(m)
        FROM Movie m
        WHERE m.releaseDate = CURRENT_DATE
          AND (:fromDate IS NULL OR m.releaseDate >= :fromDate)
          AND (:toDate IS NULL OR m.releaseDate <= :toDate)
    """)
    long countNowShowing(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    /**
     * Sắp chiếu: releaseDate > today
     */
    @Query("""
        SELECT COUNT(m)
        FROM Movie m
        WHERE m.releaseDate > CURRENT_DATE
          AND (:fromDate IS NULL OR m.releaseDate >= :fromDate)
          AND (:toDate IS NULL OR m.releaseDate <= :toDate)
    """)
    long countUpcoming(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
}
