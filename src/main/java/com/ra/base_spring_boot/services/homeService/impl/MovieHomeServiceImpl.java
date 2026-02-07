package com.ra.base_spring_boot.services.homeService.impl;

import com.ra.base_spring_boot.dto.resp.homeresp.MovieDetailResponse;
import com.ra.base_spring_boot.dto.resp.homeresp.MovieListResponse;
import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.repository.homerpo.IMovieHomeRepository;
import com.ra.base_spring_boot.services.homeService.IMovieHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieHomeServiceImpl implements IMovieHomeService {

    private final IMovieHomeRepository movieHomeRepository;

    @Override
    public Page<MovieListResponse> getNowShowingMovies(int page, int size, String sortBy, String direction) {
        Pageable pageable = buildPageable(page, size, sortBy, direction);

        Page<Movie> movies = movieHomeRepository.findByStatus(MovieStatus.NOW_SHOWING, pageable);

        return movies.map(this::toListResponse);
    }

    @Override
    public MovieDetailResponse getNowShowingMovieDetail(Long id) {
        Movie movie = movieHomeRepository.findNowShowingMovieDetail(id, MovieStatus.NOW_SHOWING)
                .orElseThrow(() -> new RuntimeException("Phim không tồn tại hoặc không chiếu"));

        return toDetailResponse(movie);
    }

    @Override
    public Page<MovieListResponse> getComingSoonMovies(int page, int size, String sortBy, String direction) {
        Pageable pageable = buildPageable(page, size, sortBy, direction);

        Page<Movie> movies = movieHomeRepository.findByStatus(MovieStatus.COMING_SOON, pageable);

        return movies.map(this::toListResponse);
    }

    // ✅ NEW: Chi tiết phim chung (mọi status)
    @Override
    public MovieDetailResponse getMovieDetail(Long id) {
        Movie movie = movieHomeRepository.findMovieDetail(id)
                .orElseThrow(() -> new RuntimeException("Phim không tồn tại"));

        return toDetailResponse(movie);
    }

    // ===================== HELPER =====================

    private Pageable buildPageable(int page, int size, String sortBy, String direction) {
        String sortField = (sortBy == null || sortBy.isBlank()) ? "releaseDate" : sortBy;

        Sort sort = Sort.by(sortField);
        sort = "desc".equalsIgnoreCase(direction) ? sort.descending() : sort.ascending();

        return PageRequest.of(Math.max(page, 0), Math.max(size, 1), sort);
    }

    private MovieListResponse toListResponse(Movie movie) {
        return MovieListResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .image(movie.getImage())
                .duration(movie.getDuration())
                .releaseDate(movie.getReleaseDate())
                .build();
    }

    private MovieDetailResponse toDetailResponse(Movie movie) {
        return MovieDetailResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescriptions())
                .author(movie.getAuthor())
                .image(movie.getImage())
                .trailer(movie.getTrailer())
                .duration(movie.getDuration())
                .releaseDate(movie.getReleaseDate())
                .type(movie.getType() == null ? null : movie.getType().toValue()) // ✅ 2D/3D đẹp
                .status(movie.getStatus() == null ? null : movie.getStatus().name())
                .genres(movie.getGenres() == null ? java.util.Set.of()
                        : movie.getGenres().stream()
                        .map(g -> g.getGenreName())
                        .collect(Collectors.toSet()))
                .build();
    }
}
