package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.MovieDTO;
import com.ra.base_spring_boot.dto.resp.MovieListResponse;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface IMovieService {

    ResponseEntity<ResponseWrapper<Page<Movie>>> getAllMovie(
            String title,
            String author,
            String type,
            Pageable pageable
    );

    ResponseEntity<ResponseWrapper<?>> createMovie(MovieDTO movieDTO, BindingResult bindingResult);

    ResponseEntity<ResponseWrapper<?>> updateMovie(Long id, MovieDTO movieDTO);

    ResponseEntity<ResponseWrapper<String>> deleteMovie(Long id);

    Movie convertMovieDTOToMovie(MovieDTO movieDTO);

    Page<MovieListResponse> getNowShowingMovies(int page, int size, String sortBy, String Direction);

}
