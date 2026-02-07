package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.constants.SeatType;
import com.ra.base_spring_boot.services.homeService.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {

    private final IMovieHomeService movieHomeService;
    private final IGenreHomeService genreHomeService;
    private final ITrailerHomeService trailerHomeService;
    private final INewsHomeService newsHomeService;
    private final IFestivalHomeService festivalHomeService;
    private final ITicketHomeService ticketHomeService;

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
     * Get Movie Detail by ID (any status)
     */
    @GetMapping("/movies/{id}")
    public ResponseEntity<?> getMovieDetail(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(movieHomeService.getMovieDetail(id))
                        .build()
        );
    }

    /**
     * Get Movie Trailer by ID (any status)
     */
    @GetMapping("/movies/{id}/trailer")
    public ResponseEntity<?> getMovieTrailer(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(trailerHomeService.getMovieTrailer(id))
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

    /**
     * Get Festivals with pagination
     */
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

    /**
     * Get Festival Detail by ID
     */
    @GetMapping("/festivals/{id}")
    public ResponseEntity<?> getFestivalDetail(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(festivalHomeService.getFestivalDetail(id))
                        .build()
        );
    }

    /**
     * Get Ticket Prices based on parameters
     */
    @GetMapping("/ticket-price")
    public ResponseEntity<?> getTicketPrices(
            @RequestParam(required = false) SeatType seatType,
            @RequestParam(required = false) MovieType movieType,
            @RequestParam(required = false) Boolean dayType,
            @RequestParam(required = false) LocalTime time
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(ticketHomeService.getTicketPrices(seatType, movieType, dayType, time))
                        .build()
        );
    }
}

