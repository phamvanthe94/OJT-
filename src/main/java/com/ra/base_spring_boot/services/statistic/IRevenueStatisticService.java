package com.ra.base_spring_boot.services.statistic;

import com.ra.base_spring_boot.dto.statistic.req.RevenueStatisticRequest;
import com.ra.base_spring_boot.dto.statistic.resp.RevenueStatisticResponse;

import java.util.List;

public interface IRevenueStatisticService {
    List<RevenueStatisticResponse> statisticRevenue(
            RevenueStatisticRequest request
    );
}
