package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.req.RevenueStatisticRequest;
import com.ra.base_spring_boot.dto.resp.RevenueStatisticResponse;

import java.util.List;

public interface IRevenueStatisticService {
    List<RevenueStatisticResponse> statisticRevenue(
            RevenueStatisticRequest request
    );
}
