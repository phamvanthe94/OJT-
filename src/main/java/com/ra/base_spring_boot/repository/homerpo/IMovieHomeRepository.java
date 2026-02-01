package com.ra.base_spring_boot.repository.homerpo;

import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.entity.movie.Genre;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IMovieHomeRepository extends JpaRepository<Movie, Long> {

    /**
     * Find movies by their status with pagination.
     */
    Page<Movie> findByStatus(MovieStatus status, Pageable pageable);

    /**
     * Find distinct genres from movies that are now showing.
     */
    @Query("""
            SELECT DISTINCT g
            FROM Movie m
            JOIN m.genres g
            WHERE m.status = :status
            AND (m.releaseDate IS NULL OR m.releaseDate <= CURRENT_DATE)
            """)
    List<Genre> findNowShowingGenres(@Param("status") MovieStatus status);

    /**
     * Find detailed information of a now showing movie by its ID.
     */
    @Query("""
                SELECT m
                FROM Movie m
                LEFT JOIN FETCH m.genres g
                WHERE m.id = :id
                AND m.status = :status
                AND (m.releaseDate IS NULL OR m.releaseDate <= CURRENT_DATE)
            """)
    Optional<Movie> findNowShowingMovieDetail(
            @Param("id") Long id,
            @Param("status") MovieStatus status
    );

    /**
     * Find a movie by its title, ignoring case.
     */
    Optional<Movie> findByTitleIgnoreCase(String title);

    /**
     * Find coming soon movies with pagination.
     */
    @Query("""
                        SELECT m
                        FROM Movie m
                        WHERE m.status = :status
                        AND m.releaseDate IS NOT NULL
                        AND m.releaseDate > CURRENT_DATE
            """)
    Page<Movie> findComingSoonMovies(
            @Param("status") MovieStatus status,
            Pageable pageable
    );


}
