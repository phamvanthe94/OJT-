package com.ra.base_spring_boot.controller.statistic;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.statistic.req.MovieStatisticRequest;
import com.ra.base_spring_boot.services.statistic.IMovieStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/movies/statistics")
@RequiredArgsConstructor
public class MovieStatisticController {

    private final IMovieStatisticService movieStatisticService;

    /**
     * Dashboard thống kê phim cho admin
     * phục vụ màn hình Dashboard của Admin
     */

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {

        MovieStatisticRequest request = MovieStatisticRequest.builder().build();

        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(movieStatisticService.getDashboardStatistic(request))
                        .build()
        );
    }
}
