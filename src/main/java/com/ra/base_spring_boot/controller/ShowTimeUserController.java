package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.services.IShowTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/showtimes")
@RequiredArgsConstructor
public class ShowTimeUserController {
        private final IShowTimeService showTimeService;

        @GetMapping
        public ResponseEntity<?> getShowTimes(@RequestParam Long movieId) {
            return ResponseEntity.ok(
                    showTimeService.getShowTimesByMovie(movieId)
            );
        }
    }
