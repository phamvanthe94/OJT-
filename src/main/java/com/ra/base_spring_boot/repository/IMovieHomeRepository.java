package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMovieHomeRepository extends JpaRepository<Movie, Long> {
    Page<Movie> findByStatus(MovieStatus status, Pageable pageable);
}
