package com.ra.base_spring_boot.services.homeService;

import com.ra.base_spring_boot.dto.resp.MovieListResponse;
import org.springframework.data.domain.Page;

public interface IMovieHomeService {

    Page<MovieListResponse> getNowShowingMovies(int page, int size, String sortBy, String Direction);
}
