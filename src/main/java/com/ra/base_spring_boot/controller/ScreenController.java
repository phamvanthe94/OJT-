package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.ScreenReq;
import com.ra.base_spring_boot.dto.resp.ScreenResp;
import com.ra.base_spring_boot.services.ScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/screens")
@RequiredArgsConstructor
public class ScreenController {
    private final ScreenService screenService;

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ScreenResp> screens = screenService.findAll(pageable);
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(screens)
                        .build()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<?> findScreenByName(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "5") int size,
                                              @RequestParam String name) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ScreenResp> screens = screenService.findByName(pageable,name);
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(screens)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findScreenById(@PathVariable Long id){
        ScreenResp screen= screenService.findById(id);
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(screen)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<?> createScreen(@RequestBody ScreenReq screenReq) {
        ScreenResp screen = screenService.createScreen(screenReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseWrapper.builder()
                        .status(HttpStatus.CREATED)
                        .code(201)
                        .data(screen)
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateScreen(@RequestBody ScreenReq screenReq, @PathVariable Long id) {
        ScreenResp screen = screenService.updateScreen(id, screenReq);
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(screen)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteScreen(@PathVariable Long id){
        screenService.deleteScreen(id);
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.NO_CONTENT)
                        .build());
    }

}
