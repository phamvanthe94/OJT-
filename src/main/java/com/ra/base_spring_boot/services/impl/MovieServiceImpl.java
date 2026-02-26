package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.MovieRequest;
import com.ra.base_spring_boot.dto.resp.MovieResponse;
import com.ra.base_spring_boot.repository.IGenreRepository;
import com.ra.base_spring_boot.repository.IMovieRepository;
import com.ra.base_spring_boot.services.IMovieService;
import com.ra.base_spring_boot.services.more.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements IMovieService {


    private final IMovieRepository movieRepository;


    private final IGenreRepository genreRepository;

    private final CloudinaryService cloudinaryService;

    @Override
    public Page<MovieResponse> getAllMovies(String title, String author, String type, String status, int page, int size, String sortBy, String direction) {
        return null;
    }

    @Override
    public MovieResponse createMovie(MovieRequest request) {
        return null;
    }

    @Override
    public MovieResponse updateMovie(Long id, MovieRequest request) {
        return null;
    }

    @Override
    public void deleteMovie(Long id) {

    }


}