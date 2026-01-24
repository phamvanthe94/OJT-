package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.req.TheaterReq.TheaterReq;
import com.ra.base_spring_boot.dto.resp.TheaterResp.TheaterResp;
import com.ra.base_spring_boot.model.entity.theater.Theater;
import com.ra.base_spring_boot.services.Theater.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        return ResponseEntity.status(200).body(theaters);
    }
    @PostMapping()
    public ResponseEntity<?> createTheater(@RequestBody TheaterReq theaterReq) {
        TheaterResp theaterResp= theaterService.createTheater(theaterReq);

        return ResponseEntity.status(201).body(theaterResp);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTheater(@RequestBody TheaterReq theaterReq,@PathVariable long id) {
        TheaterResp theaterResp= theaterService.updateTheater(id, theaterReq);

        return ResponseEntity.status(200).body(theaterResp);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTheater(@PathVariable long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getTheaterById(@PathVariable long id) {
        TheaterResp theaterResp= theaterService.getTheaterById(id);
        return ResponseEntity.status(200).body(theaterResp);
    }
    @GetMapping("/search")
    public ResponseEntity<Page<TheaterResp>> search(
            @RequestParam String name,
            Pageable pageable
    ) {
        Page<TheaterResp> result =
                theaterService.searchTheaterByName(name, pageable);

        return ResponseEntity.ok(result);
    }
}
