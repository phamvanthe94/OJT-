package com.ra.base_spring_boot.controller;


import com.ra.base_spring_boot.services.statisticService.INewAndFestivalStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/statistic/news-festival")
@RequiredArgsConstructor
public class NewAndFestivalStatisticController {

    private final INewAndFestivalStatisticService statisticService;

    @GetMapping
    public ResponseEntity<?> statistic() {
        return ResponseEntity.ok(
                statisticService.statisticNewsAndFestival()
        );
    }
}
