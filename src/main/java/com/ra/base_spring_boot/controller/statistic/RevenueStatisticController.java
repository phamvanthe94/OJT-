package com.ra.base_spring_boot.controller.statistic;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.statistic.req.RevenueStatisticRequest;
import com.ra.base_spring_boot.dto.statistic.resp.RevenueStatisticResponse;
import com.ra.base_spring_boot.services.statistic.IRevenueStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/statistic/revenues")
@RequiredArgsConstructor

public class RevenueStatisticController {

    private final IRevenueStatisticService service;

    @GetMapping()
    public ResponseEntity<?> getRevenue(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) Long theaterId,
            @RequestParam(required = false) Long screenId
    ) {
        if (movieId == null && genreId == null && theaterId == null && screenId == null) {
            return ResponseEntity.badRequest().body(
                    ResponseWrapper.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .code(400)
                            .build()
            );
        }

        RevenueStatisticRequest request = new RevenueStatisticRequest();
        request.setMovieId(movieId);
        request.setGenreId(genreId);
        request.setTheaterId(theaterId);
        request.setScreenId(screenId);

        List<RevenueStatisticResponse> response = service.getRevenue(request);

        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(response)
                        .build()
        );
    }
}
