package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.constants.SeatType;
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
    private final ITicketHomeService ticketHomeService;


    @GetMapping("/genres/now-showing")
    public ResponseEntity<?> getNowShowingGenres() {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(genreHomeService.getNowShowingGenres())
                        .build()
        );
    }


    @GetMapping("/movies/now-showing")
    public ResponseEntity<?> getNowShowingMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(movieHomeService.getNowShowingMovies(page, size, sortBy, direction))
                        .build()
        );
    }


    @GetMapping("/movies/now-showing/{id}")
    public ResponseEntity<?> getNowShowingMovieDetail(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(movieHomeService.getNowShowingMovieDetail(id))
                        .build()
        );
    }


    @GetMapping("/movies/{id}/trailer")
    public ResponseEntity<?> getMovieTrailer(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(trailerHomeService.getMovieTrailer(id))
                        .build()
        );
    }


    @GetMapping("/movies/coming-soon")
    public ResponseEntity<?> getComingSoonMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(movieHomeService.getComingSoonMovies(page, size, sortBy, direction))
                        .build()
        );
    }


    @GetMapping("/news")
    public ResponseEntity<?> getHomeNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(newsHomeService.getHomeNews(page, size))
                        .build()
        );
    }

    @GetMapping("/news/{id}")
    public ResponseEntity<?> getHomeNewsDetail(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
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
                        .code(HttpStatus.OK.value())
                        .data(festivalHomeService.getFestivals(page, size))
                        .build()
        );
    }

    @GetMapping("/festivals/{id}")
    public ResponseEntity<?> getFestivalDetail(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(festivalHomeService.getFestivalDetail(id))
                        .build()
        );
    }


    @GetMapping("/ticket-price")
    public ResponseEntity<?> getTicketPrices(
            @RequestParam(required = false) SeatType seatType,
            @RequestParam(required = false) MovieType movieType,
            @RequestParam(required = false) Boolean dayType
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(ticketHomeService.getTicketPrices(seatType, movieType, dayType))
                        .build()
        );
    }
}
