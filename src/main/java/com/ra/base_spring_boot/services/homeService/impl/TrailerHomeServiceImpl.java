package com.ra.base_spring_boot.services.homeService.impl;

import com.ra.base_spring_boot.dto.resp.homeresp.MovieTrailerResponse;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.repository.homerpo.IMovieHomeRepository;
import com.ra.base_spring_boot.services.homeService.ITrailerHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrailerHomeServiceImpl implements ITrailerHomeService {

    private final IMovieHomeRepository movieHomeRepository;

    @Override
    public MovieTrailerResponse getNowShowingMovieTrailer(Long id) {
        Movie movie = movieHomeRepository.findMovieDetailByIdAndStatus(id, MovieStatus.NOW_SHOWING)
                .orElseThrow(() -> new HttpNotFound("Now-showing movie not found"));

        return toTrailerResponse(movie);
    }

    @Override
    public MovieTrailerResponse getMovieTrailer(Long id) {
        Movie movie = movieHomeRepository.findMovieDetail(id)
                .orElseThrow(() -> new HttpNotFound("Movie not found"));

        return toTrailerResponse(movie);
    }

    private MovieTrailerResponse toTrailerResponse(Movie movie) {
        if (movie.getTrailer() == null || movie.getTrailer().isBlank()) {
            throw new HttpNotFound("Movie trailer not found");
        }

        return MovieTrailerResponse.builder()
                .movieId(movie.getId())
                .title(movie.getTitle())
                .trailer(movie.getTrailer())
                .build();
    }
}
