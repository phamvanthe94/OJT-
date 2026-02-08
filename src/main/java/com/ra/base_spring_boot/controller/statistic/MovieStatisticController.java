package com.ra.base_spring_boot.controller.statistic;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.statistic.req.MovieStatisticRequest;
import com.ra.base_spring_boot.services.statistic.IMovieStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/admin/movies/statistics")
@RequiredArgsConstructor
public class MovieStatisticController {

    private final IMovieStatisticService movieStatisticService;

    /**
     * Get Movie Statistics
     */
    @GetMapping
    public ResponseEntity<?> getMovieStatistics(
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate
    ) {

        MovieStatisticRequest request = MovieStatisticRequest.builder()
                .fromDate(fromDate)
                .toDate(toDate)
                .build();

        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(movieStatisticService.getMovieStatistics(request))
                        .build()
        );
    }
}
