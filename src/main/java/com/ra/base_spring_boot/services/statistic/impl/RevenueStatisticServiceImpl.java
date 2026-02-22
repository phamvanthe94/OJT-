package com.ra.base_spring_boot.services.statistic.impl;

import com.ra.base_spring_boot.dto.statistic.req.RevenueStatisticRequest;
import com.ra.base_spring_boot.dto.statistic.resp.RevenueStatisticResponse;
import com.ra.base_spring_boot.model.constants.BookingStatus;
import com.ra.base_spring_boot.repository.statistic.IRevenueStatisticRepository;
import com.ra.base_spring_boot.services.statistic.IRevenueStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RevenueStatisticServiceImpl implements IRevenueStatisticService {

    private final IRevenueStatisticRepository repository;


    @Override
    public List<RevenueStatisticResponse> getRevenue(RevenueStatisticRequest request) {
        if (request.getMovieId() == null &&
                request.getGenreId() == null &&
                request.getTheaterId() == null &&
                request.getScreenId() == null) {
            throw new RuntimeException("Hãy điền thông tin vào nhóm cần tìm");
        }

        LocalDate today = LocalDate.now();
        LocalDateTime startToday = today.atStartOfDay();
        LocalDateTime endToday = today.atTime(LocalTime.MAX);
        LocalDateTime startWeek = today.with(java.time.DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime startMonth = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime startYear = today.withDayOfYear(1).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();

        List<RevenueStatisticResponse> result = Collections.emptyList();

        if (request.getMovieId() != null) {
            result = repository.revenueByMovie(
                    BookingStatus.COMPLETED, request.getMovieId(),
                    startToday, endToday, startWeek, startMonth, startYear, now
            );
        } else if (request.getGenreId() != null) {
            result = repository.revenueByGenre(
                    BookingStatus.COMPLETED, request.getGenreId(),
                    startToday, endToday, startWeek, startMonth, startYear, now
            );
        } else if (request.getTheaterId() != null) {
            result = repository.revenueByTheater(
                    BookingStatus.COMPLETED, request.getTheaterId(),
                    startToday, endToday, startWeek, startMonth, startYear, now
            );
        } else if (request.getScreenId() != null) {
            result = repository.revenueByScreen(
                    BookingStatus.COMPLETED, request.getScreenId(),
                    startToday, endToday, startWeek, startMonth, startYear, now
            );
        }

        return result != null ? result : Collections.emptyList();
    }
}
