package com.ra.base_spring_boot.services.statistic.impl;

import com.ra.base_spring_boot.dto.statistic.req.RevenueStatisticRequest;
import com.ra.base_spring_boot.dto.statistic.resp.RevenueStatisticResponse;
import com.ra.base_spring_boot.repository.Statistic.IRevenueStatisticRepository;
import com.ra.base_spring_boot.services.statistic.IRevenueStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class RevenueStatisticServiceImpl implements IRevenueStatisticService {

    private final IRevenueStatisticRepository repository;

    @Override
    public List<RevenueStatisticResponse> statisticRevenue(RevenueStatisticRequest request) {
        LocalDateTime from = request.getFromDate().atStartOfDay();
        LocalDateTime to = request.getToDate().atTime(23, 59, 59);

        List<Object[]> rawData;

        switch (request.getGroupBy()) {
            case "MOVIE":
                rawData = repository.revenueByMovie(from, to);
                break;
            case "GENRE":
                rawData = repository.revenueByGenre(from, to);
                break;
            case "DATE":
                rawData = repository.revenueByDate(from, to);
                break;
            case "MONTH":
                rawData = repository.revenueByMonth(from, to);
                break;
            case "YEAR":
                rawData = repository.revenueByYear(from, to);
                break;
            default:
                throw new IllegalArgumentException("groupBy không hợp lệ");
        }

        return rawData.stream()
                .map(o -> RevenueStatisticResponse.builder()
                        .label(String.valueOf(o[0]))
                        .revenue((Double) o[1])
                        .build())
                .toList();
    }
}
