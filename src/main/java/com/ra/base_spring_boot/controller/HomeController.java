package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.services.homeService.IGenreHomeService;
import com.ra.base_spring_boot.services.homeService.IMovieHomeService;
import com.ra.base_spring_boot.services.homeService.ITrailerHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {

    private final IMovieHomeService movieHomeService;
    private final IGenreHomeService genreHomeService;
    private final ITrailerHomeService trailerHomeService;

    /**
     * Get Now Showing Movies with pagination and sorting
     */

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


    /**
     * Get Genres of Now Showing Movies
     */
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


    /**
     * Get Now Showing Movie Detail by ID
     */
    @GetMapping("/movies/now-showing/{id}")
    public ResponseEntity<?> getNowShowingMovieDetail(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(movieHomeService.getNowShowingMovieDetail(id))
                        .build()
        );
    }


    /**
     * Get Now Showing Movie Trailer by ID
     */
    @GetMapping("/movies/now-showing/{id}/trailer")
    public ResponseEntity<?> getNowShowingMovieTrailer(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(trailerHomeService.getNowShowingMovieTrailer(id))
                        .build()
        );
    }


    /**
     * Get Coming Soon Movies with pagination and sorting
     */
    @GetMapping("/movies/coming-soon")
    public ResponseEntity<?> getComingSoonMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(movieHomeService.getComingSoonMovies(page, size, sortBy, direction))
                        .build()
        );
    }
}

