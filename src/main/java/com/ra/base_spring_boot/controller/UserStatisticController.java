package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.resp.UserStatisticResponse;
import com.ra.base_spring_boot.services.IUserStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user-statistics")
@RequiredArgsConstructor

public class UserStatisticController {
    private final IUserStatisticService userStatisticService;

    @GetMapping
    public ResponseEntity<UserStatisticResponse> getUserStatistics() {
        return ResponseEntity.ok(userStatisticService.getUserStatistic());
    }
}
