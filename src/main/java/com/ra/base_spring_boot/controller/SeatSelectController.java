package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.resp.SeatSelectResp;
import com.ra.base_spring_boot.services.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/seats")
@RequiredArgsConstructor
public class SeatSelectController {

    private final SeatService seatService;

    @GetMapping("/select")
    public ResponseEntity<List<SeatSelectResp>> getSeatsByShowTime(
            @RequestParam Long showTimeId
    ) {
        return ResponseEntity.ok(
                seatService.getSeatsByShowTime(showTimeId)
        );
    }
}

