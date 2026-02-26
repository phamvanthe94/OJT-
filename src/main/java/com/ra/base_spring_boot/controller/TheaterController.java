package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.TheaterReq.TheaterReq;
import com.ra.base_spring_boot.dto.resp.TheaterResp.TheaterResp;
import com.ra.base_spring_boot.model.entity.theater.Theater;
import com.ra.base_spring_boot.services.Theater.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/theater")
public class TheaterController {
    @Autowired
    private TheaterService theaterService;

    @GetMapping()
    public ResponseEntity<?> getTheater(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable= PageRequest.of(page, size);
        Page<TheaterResp> theaters = theaterService.getTheaters(pageable);

        return ResponseEntity.status(200).body(ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(theaters)
                .build());
    }
    @PostMapping()
    public ResponseEntity<?> createTheater(@RequestBody TheaterReq theaterReq) {
        TheaterResp theaterResp= theaterService.createTheater(theaterReq);

        return ResponseEntity.status(201).body(ResponseWrapper.builder()
                .status(HttpStatus.CREATED)
                .code(201)
                .data(theaterResp)
                .build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTheater(@RequestBody TheaterReq theaterReq,@PathVariable long id) {
        TheaterResp theaters= theaterService.updateTheater(id, theaterReq);

        return ResponseEntity.status(200).body(ResponseWrapper.builder()
                .status(HttpStatus.OK)
                .code(200)
                .data(theaters)
                .build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTheater(@PathVariable long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.status(204).body(ResponseWrapper.builder()
                .status(HttpStatus.NO_CONTENT)
                .code(204)
                .data(null)
                .build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getTheaterById(@PathVariable long id) {
        TheaterResp theaters= theaterService.getTheaterById(id);
        return ResponseEntity.status(200).body(ResponseWrapper.builder()
                .status(HttpStatus.OK)
                .code(200)
                .data(theaters)
                .build());
    }
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable= PageRequest.of(page, size);
        Page<TheaterResp> result =
                theaterService.searchTheaterByName(name, pageable);

        return ResponseEntity.status(200).body(ResponseWrapper.builder()
                .status(HttpStatus.OK)
                .code(200)
                .data(result)
                .build());
    }
}
