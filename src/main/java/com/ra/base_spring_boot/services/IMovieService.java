package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.req.MovieRequest;
import com.ra.base_spring_boot.dto.resp.MovieResponse;
import org.springframework.data.domain.Page;

public interface IMovieService {

    // get all (search + sort + page)
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

    //  create
    MovieResponse createMovie(MovieRequest request);

    // update
    MovieResponse updateMovie(Long id, MovieRequest request);

    // delete
    void deleteMovie(Long id);
}
