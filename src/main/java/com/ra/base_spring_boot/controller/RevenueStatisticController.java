package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.req.RevenueStatisticRequest;
import com.ra.base_spring_boot.dto.resp.RevenueStatisticResponse;
import com.ra.base_spring_boot.services.IRevenueStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/statistic/revenue")
@RequiredArgsConstructor

public class RevenueStatisticController {
    private final IRevenueStatisticService revenueStatisticService;

    @PostMapping
    public List<RevenueStatisticResponse> statisticRevenue(
            @RequestBody RevenueStatisticRequest request
    ) {
        return revenueStatisticService.statisticRevenue(request);
    }

}
