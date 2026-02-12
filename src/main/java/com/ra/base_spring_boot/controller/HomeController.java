package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.constants.SeatType;
import com.ra.base_spring_boot.services.homeService.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

    // ===================== THỂ LOẠI (phim đang công chiếu) =====================

    @GetMapping("/genres/now-showing")
    public ResponseEntity<?> getNowShowingGenres() {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(genreHomeService.getNowShowingGenres())
                        .build()
        );
    }

    // ===================== PHIM ĐANG CÔNG CHIẾU =====================

    @GetMapping("/movies/now-showing")
    public ResponseEntity<?> getNowShowingMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(movieHomeService.getNowShowingMovies(page, size, sortBy, direction))
                        .build()
        );
    }

    @GetMapping("/movies/now-showing/{id}")
    public ResponseEntity<?> getNowShowingMovieDetail(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(movieHomeService.getNowShowingMovieDetail(id))
                        .build()
        );
    }

    @GetMapping("/movies/now-showing/{id}/trailer")
    public ResponseEntity<?> getNowShowingMovieTrailer(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(trailerHomeService.getNowShowingMovieTrailer(id))
                        .build()
        );
    }

    // ===================== PHIM SẮP CHIẾU =====================

    @GetMapping("/movies/coming-soon")
    public ResponseEntity<?> getComingSoonMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(movieHomeService.getComingSoonMovies(page, size, sortBy, direction))
                        .build()
        );
    }

    // ===================== PHIM CHI TIẾT & TRAILER (CHUNG) =====================

    @GetMapping("/movies/{id}")
    public ResponseEntity<?> getMovieDetail(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(movieHomeService.getMovieDetail(id))
                        .build()
        );
    }

    @GetMapping("/movies/{id}/trailer")
    public ResponseEntity<?> getMovieTrailer(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(trailerHomeService.getMovieTrailer(id))
                        .build()
        );
    }

    // ===================== TIN TỨC =====================

    @GetMapping("/news")
    public ResponseEntity<?> getHomeNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(newsHomeService.getHomeNews(page, size))
                        .build()
        );
    }

    @GetMapping("/news/{id}")
    public ResponseEntity<?> getHomeNewsDetail(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(newsHomeService.getHomeNewDetail(id))
                        .build()
        );
    }

    // ===================== FESTIVAL =====================

    @GetMapping("/festivals")
    public ResponseEntity<?> getFestivals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(festivalHomeService.getFestivals(page, size))
                        .build()
        );
    }

    @GetMapping("/festivals/{id}")
    public ResponseEntity<?> getFestivalDetail(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(festivalHomeService.getFestivalDetail(id))
                        .build()
        );
    }

    // ===================== GIÁ VÉ =====================

    @GetMapping("/ticket-price")
    public ResponseEntity<?> getTicketPrices(
            @RequestParam(required = false) SeatType seatType,
            @RequestParam(required = false) MovieType movieType,
            @RequestParam(required = false) Boolean dayType,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            @Parameter(schema = @Schema(type = "string", format = "time", example = "09:00:00"))
            LocalTime time
    ) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(ticketHomeService.getTicketPrices(seatType, movieType, dayType, time))
                        .build()
        );
    }
}
