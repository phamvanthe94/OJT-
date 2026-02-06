package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticController {
    @Autowired
    private StatisticService statisticService;

    @GetMapping("/news-festival")
    public ResponseEntity<ResponseWrapper<?>> statisticNewsFestivalDetail() {
        return statisticService.statisticNewsAndFestivalDetail();
    }
}
