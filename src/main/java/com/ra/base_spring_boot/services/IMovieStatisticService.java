package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.req.MovieStatisticRequest;
import com.ra.base_spring_boot.dto.resp.MovieStatisticResponse;

public interface IMovieStatisticService {
    MovieStatisticResponse getMovieStatistics(MovieStatisticRequest request);
}
