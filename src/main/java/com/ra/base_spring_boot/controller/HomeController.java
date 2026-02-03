package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.services.homeService.*;
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
    private final INewsHomeService newsHomeService;
    private final IFestivalHomeService festivalHomeService;

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


    /**
     * Get Home News with pagination
     */
    @GetMapping("/news")
    public ResponseEntity<?> getHomeNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(newsHomeService.getHomeNews(page, size))
                        .build()
        );
    }


    /**
     * Get Home News Detail by ID
     */
    @GetMapping("/news/{id}")
    public ResponseEntity<?> getHomeNewsDetail(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(newsHomeService.getHomeNewDetail(id))
                        .build()
        );
    }

    @GetMapping("/festivals")
    public ResponseEntity<?> getFestivals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(festivalHomeService.getFestivals(page, size))
                        .build()
        );
    }
}

