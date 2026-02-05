package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;

public interface IMovieStatisticRepository extends JpaRepository<Movie, Long> {

    // Đếm phim đã chiếu
    @Query("SELECT COUNT(m) FROM Movie m WHERE m.status = :status AND m.releaseDate <= :today AND m.releaseDate >= :fromDate AND m.releaseDate <= :toDate")
    long countReleased(@Param("status") MovieStatus status,
                       @Param("today") LocalDate today,
                       @Param("fromDate") LocalDate fromDate,
                       @Param("toDate") LocalDate toDate);

    // Đếm phim sắp chiếu
    @Query("SELECT COUNT(m) FROM Movie m WHERE m.releaseDate > :today AND m.releaseDate >= :fromDate AND m.releaseDate <= :toDate")
    long countUpcoming(@Param("today") LocalDate today,
                       @Param("fromDate") LocalDate fromDate,
                       @Param("toDate") LocalDate toDate);

    // Đếm phim đang chiếu (ví dụ releaseDate <= today và releaseDate >= today - 1 tháng)
    @Query("SELECT COUNT(m) FROM Movie m WHERE m.releaseDate <= :today AND m.releaseDate >= :fromShowingDate AND m.releaseDate <= :toDate")
    long countNowShowing(@Param("today") LocalDate today,
                         @Param("fromShowingDate") LocalDate fromShowingDate,
                         @Param("toDate") LocalDate toDate);
}
