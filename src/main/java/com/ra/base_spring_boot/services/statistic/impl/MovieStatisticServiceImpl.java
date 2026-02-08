package com.ra.base_spring_boot.services.statistic.impl;

import com.ra.base_spring_boot.dto.statistic.req.MovieStatisticRequest;
import com.ra.base_spring_boot.dto.statistic.resp.MovieStatisticResponse;
import com.ra.base_spring_boot.repository.Statistic.IMovieStatisticRepository;
import com.ra.base_spring_boot.services.statistic.IMovieStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MovieStatisticServiceImpl implements IMovieStatisticService {

    private final IMovieStatisticRepository movieStatisticRepository;

    @Override
    public MovieStatisticResponse getMovieStatistics(MovieStatisticRequest request) {

        LocalDate fromDate = request.getFromDate();
        LocalDate toDate = request.getToDate();

        long released = movieStatisticRepository.countReleased(fromDate, toDate);
        long nowShowing = movieStatisticRepository.countNowShowing(fromDate, toDate);
        long upcoming = movieStatisticRepository.countUpcoming(fromDate, toDate);

        return MovieStatisticResponse.builder()
                .released(released)
                .nowShowing(nowShowing)
                .upcoming(upcoming)
                .build();
    }
}
