package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.req.MovieRequest;
import com.ra.base_spring_boot.dto.resp.MovieResponse;
import org.springframework.data.domain.Page;

public interface IMovieService {

    Page<MovieResponse> getAllMovies(
            String title,
            String author,
            String type,
            String status,
            int page,
            int size,
            String sortBy,
            String direction
    );

    MovieResponse createMovie(MovieRequest request);

    MovieResponse updateMovie(Long id, MovieRequest request);

    void deleteMovie(Long id);
}
