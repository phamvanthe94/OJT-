package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.services.homeService.IGenreHomeService;
import com.ra.base_spring_boot.services.homeService.IMovieHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {

    private final IMovieHomeService movieHomeService;
    private final IGenreHomeService genreHomeService;

    @GetMapping("/movies/now-showing")
    public ResponseEntity<?> getNowShowingMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(movieHomeService.getNowShowingMovies(page, size, sortBy, direction))
                        .build()
        );
    }

    @GetMapping("/genres/now-showing")
    public ResponseEntity<?> getNowShowingGenres() {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(genreHomeService.getNowShowingGenres())
                        .build()
        );
    }
}

