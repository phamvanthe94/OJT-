package com.ra.base_spring_boot.controller.statistic;

import com.ra.base_spring_boot.dto.statistic.req.RevenueStatisticRequest;
import com.ra.base_spring_boot.dto.statistic.resp.RevenueStatisticResponse;
import com.ra.base_spring_boot.services.statistic.IRevenueStatisticService;
import lombok.RequiredArgsConstructor;
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
