package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.MovieReq;
import com.ra.base_spring_boot.services.IMovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/movies")
@RequiredArgsConstructor
public class MovieController {

    private final IMovieService movieService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String type,
            org.springframework.data.domain.Pageable pageable
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(movieService.getAllMovies(title,author,type,null,pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().toString(), "asc"))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(movieService.(id))
                        .build()
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@Valid @ModelAttribute MovieReq req, BindingResult br) {
        if (br.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    ResponseWrapper.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .code(400)
                            .data(br.getFieldError().getDefaultMessage())
                            .build()
            );
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseWrapper.builder()
                        .status(HttpStatus.CREATED)
                        .code(201)
                        .data(movieService.create(req))
                        .build()
        );
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @ModelAttribute MovieReq req, BindingResult br) {
        if (br.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    ResponseWrapper.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .code(400)
                            .data(br.getFieldError().getDefaultMessage())
                            .build()
            );
        }

        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(movieService.update(id, req))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data("Delete movie successfully")
                        .build()
        );
    }
}
