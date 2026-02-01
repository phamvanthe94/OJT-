package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.entity.movie.Genre;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMovieHomeRepository extends JpaRepository<Movie, Long> {

    Page<Movie> findByStatus(MovieStatus status, Pageable pageable);

    @Query("""
            SELECT DISTINCT g
            FROM Movie m
            JOIN m.genres g
            WHERE m.status = :status
            AND (m.releaseDate IS NULL OR m.releaseDate <= CURRENT_DATE)
            """)
    List<Genre> findNowShowingGenres(@Param("status") MovieStatus status);
}
