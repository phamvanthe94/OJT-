package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.MovieDTO;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.services.CloudinaryService;
import com.ra.base_spring_boot.services.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @GetMapping
    public ResponseEntity<ResponseWrapper<Page<Movie>>> getAllMovie(@RequestParam(name="title", required = false) String title,
                                                                    @RequestParam(name="author", required = false) String author,
                                                                    @RequestParam(name="type", required = false) String type,
                                                                    @RequestParam(name="page", defaultValue = "0") int page,
                                                                    @RequestParam(name="size", defaultValue = "5") int size){
        return movieService.getAllMovie(title, author, type, PageRequest.of(page, size));
    }
    @PostMapping("/add")
    public ResponseEntity<ResponseWrapper<?>> addMovie(@Valid @ModelAttribute MovieDTO movieDTO, BindingResult bindingResult){
        ResponseEntity<ResponseWrapper<?>> responseEntity = movieService.createMovie(movieDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    ResponseWrapper
                            .<Object>builder()
                            .data(bindingResult.getAllErrors())
                            .code(400)
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        }
        return responseEntity;
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseWrapper<?>> updateMovie(@PathVariable Long id, @Valid @ModelAttribute MovieDTO movieDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    ResponseWrapper
                            .<Object>builder()
                            .data(bindingResult.getAllErrors())
                            .code(400)
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        }
        return movieService.updateMovie(id, movieDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteMovie(@PathVariable Long id) {
        return movieService.deleteMovie(id);
    }
}
