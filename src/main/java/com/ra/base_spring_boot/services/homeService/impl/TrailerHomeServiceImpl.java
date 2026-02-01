package com.ra.base_spring_boot.services.homeService.impl;

import com.ra.base_spring_boot.dto.resp.homeresp.MovieTrailerResponse;
import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.repository.IMovieHomeRepository;
import com.ra.base_spring_boot.services.homeService.ITrailerHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrailerHomeServiceImpl implements ITrailerHomeService {

    private final IMovieHomeRepository movieHomeRepository;

    @Override
    public MovieTrailerResponse getNowShowingMovieTrailer(Long id) {

        Movie movie = movieHomeRepository.findNowShowingMovieDetail(id, MovieStatus.NOW_SHOWING)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim đang chiếu !"));

        if (movie.getTrailer() == null || movie.getTrailer().isBlank()) {
            throw new RuntimeException("Phim hiện không có trailer !");
        }

        return MovieTrailerResponse.builder()
                .movieId(movie.getId())
                .title(movie.getTitle())
                .trailer(movie.getTrailer())
                .build();
    }
}
