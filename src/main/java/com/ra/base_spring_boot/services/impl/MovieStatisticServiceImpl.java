package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.MovieStatisticRequest;
import com.ra.base_spring_boot.dto.resp.MovieStatisticResponse;
import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.repository.IMovieStatisticRepository;
import com.ra.base_spring_boot.services.IMovieStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor

public class MovieStatisticServiceImpl implements IMovieStatisticService {

    private final IMovieStatisticRepository iMovieStatisticRepository;

    @Override
    public MovieStatisticResponse getMovieStatistics(MovieStatisticRequest request) {

        LocalDate today = LocalDate.now();
        LocalDate fromDate = request.getFromDate() != null ? request.getFromDate() : LocalDate.of(2000, 1, 1);
        LocalDate toDate = request.getToDate() != null ? request.getToDate() : today;
        LocalDate fromShowingDate = today.minusMonths(1); // Định nghĩa phim đang chiếu

        long released = iMovieStatisticRepository.countReleased(MovieStatus.NOW_SHOWING, today, fromDate, toDate);
        long upcoming = iMovieStatisticRepository.countUpcoming(today, fromDate, toDate);
        long nowShowing = iMovieStatisticRepository.countNowShowing(today, fromShowingDate, toDate);

        return MovieStatisticResponse.builder()
                .released(released)
                .upcoming(upcoming)
                .nowShowing(nowShowing)
                .build();
    }
}
