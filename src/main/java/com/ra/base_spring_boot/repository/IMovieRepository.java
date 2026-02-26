package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IMovieRepository extends JpaRepository<Movie, Long> {
    @Query("""
        SELECT m FROM Movie m
        WHERE (:title IS NULL OR m.title LIKE %:title%)
          AND (:author IS NULL OR m.author LIKE %:author%)
          AND (:type IS NULL OR m.type = :type)
          AND (:status IS NULL OR m.status = :status)
    """)
    Page<Movie> search(
            @Param("title") String title,
            @Param("author") String author,
            @Param("type") MovieType type,
            @Param("status") MovieStatus status,
            Pageable pageable
    );
}

