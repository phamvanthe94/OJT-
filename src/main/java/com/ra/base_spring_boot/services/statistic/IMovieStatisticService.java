package com.ra.base_spring_boot.services.statistic;

import com.ra.base_spring_boot.dto.statistic.req.MovieStatisticRequest;
import com.ra.base_spring_boot.dto.statistic.resp.MovieStatisticResponse;

public interface IMovieStatisticService {
    MovieStatisticResponse getDashboardStatistic(MovieStatisticRequest request);
}
