package com.ra.base_spring_boot.controller.statistic;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.statistic.req.UserStatisticRequest;
import com.ra.base_spring_boot.services.statistic.IUserStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/user/statistics")
@RequiredArgsConstructor

public class UserStatisticController {
    private final IUserStatisticService userStatisticService;

    /**
     * Dashboard thống kê user cho admin
     * phục vụ màn hình Dashboard của Admin
     */
    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {

        UserStatisticRequest request = UserStatisticRequest.builder().build();

        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(userStatisticService.getDashboardStatistic(request))
                        .build()
        );
    }
}
