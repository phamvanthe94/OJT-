package com.ra.base_spring_boot.services.homeService.impl;

import com.ra.base_spring_boot.dto.resp.GenreResponse;
import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.entity.movie.Genre;
import com.ra.base_spring_boot.repository.IMovieHomeRepository;
import com.ra.base_spring_boot.services.homeService.IGenreHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreHomeServiceImpl implements IGenreHomeService {

    private final IMovieHomeRepository movieHomeRepository;

    @Override
    public List<GenreResponse> getNowShowingGenres() {

        List<Genre> genres = movieHomeRepository.findNowShowingGenres(MovieStatus.NOW_SHOWING);

        return genres.stream()
                .map(genre -> GenreResponse.builder()
                        .id(genre.getId())
                        .genreName(genre.getGenreName())
                        .build())
                .toList();
    }
}
