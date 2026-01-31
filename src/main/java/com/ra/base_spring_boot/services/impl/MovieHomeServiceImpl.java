package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.resp.MovieListResponse;
import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.repository.IMovieHomeRepository;
import com.ra.base_spring_boot.services.IMovieHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieHomeServiceImpl implements IMovieHomeService {

    private final IMovieHomeRepository movieHomeRepository;

    @Override
    public Page<MovieListResponse> getNowShowingMovies(int page, int size, String sortBy, String direction) {
        String sortField = (sortBy == null || sortBy.isBlank()) ? "releaseDate" : sortBy;

        Sort sort = Sort.by(sortField);
        sort = "desc".equalsIgnoreCase(direction) ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), sort);

        Page<Movie> movies = movieHomeRepository.findByStatus(MovieStatus.NOW_SHOWING, pageable);

        return movies.map(movie -> MovieListResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .image(movie.getImage())
                .duration(movie.getDuration())
                .releaseDate(movie.getReleaseDate())
                .build());
    }
}
