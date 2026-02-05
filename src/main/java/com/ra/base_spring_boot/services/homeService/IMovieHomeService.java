package com.ra.base_spring_boot.services.homeService;

import com.ra.base_spring_boot.dto.resp.homeresp.MovieDetailResponse;
import com.ra.base_spring_boot.dto.resp.homeresp.MovieListResponse;
import org.springframework.data.domain.Page;

public interface IMovieHomeService {

    Page<MovieListResponse> getNowShowingMovies(int page, int size, String sortBy, String Direction);

    MovieDetailResponse getNowShowingMovieDetail(Long id);

    Page<MovieListResponse> getComingSoonMovies(int page, int size, String sortBy, String Direction);
}
