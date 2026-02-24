package com.ra.base_spring_boot.services.statistic.impl;

import com.ra.base_spring_boot.dto.statistic.req.MovieStatisticRequest;
import com.ra.base_spring_boot.dto.statistic.resp.MovieStatisticResponse;
import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.repository.statistic.IMovieStatisticRepository;
import com.ra.base_spring_boot.services.statistic.IMovieStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieStatisticServiceImpl implements IMovieStatisticService {

    private final IMovieStatisticRepository movieStatisticRepository;

    @Override
    public MovieStatisticResponse getDashboardStatistic(MovieStatisticRequest request) {

        return MovieStatisticResponse.builder()
                .comingSoon(movieStatisticRepository.countByStatus(MovieStatus.COMING_SOON))
                .nowShowing(movieStatisticRepository.countByStatus(MovieStatus.NOW_SHOWING))
                .offline(movieStatisticRepository.countByStatus(MovieStatus.OFFLINE))
                .build();

    }
}
