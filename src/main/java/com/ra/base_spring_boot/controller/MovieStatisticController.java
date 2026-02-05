package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.req.MovieStatisticRequest;
import com.ra.base_spring_boot.dto.resp.MovieStatisticResponse;
import com.ra.base_spring_boot.services.IMovieStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movies/statistics")
@RequiredArgsConstructor
public class MovieStatisticController {

    private final IMovieStatisticService movieStatisticService;

    // Chỉ admin mới được truy cập
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MovieStatisticResponse> getStatistics(@RequestBody MovieStatisticRequest request) {
        MovieStatisticResponse response = movieStatisticService.getMovieStatistics(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<MovieStatisticResponse> getStatistics() {
        MovieStatisticResponse response = movieStatisticService.getMovieStatistics(new MovieStatisticRequest());
        return ResponseEntity.ok(response);
    }
}
