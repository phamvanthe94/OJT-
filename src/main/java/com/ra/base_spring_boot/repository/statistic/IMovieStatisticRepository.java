package com.ra.base_spring_boot.repository.statistic;

import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMovieStatisticRepository extends JpaRepository<Movie, Long> {

    long countByStatus(MovieStatus status);
}
