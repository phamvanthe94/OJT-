package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.ShowTimeRequest;
import com.ra.base_spring_boot.services.IShowTimeAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/showtimes")
@RequiredArgsConstructor
public class ShowTimeController {

    private final IShowTimeAdminService showTimeAdminService;

    @GetMapping
    public ResponseEntity<?> handleGetAllShowTimes(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(showTimeAdminService.getAllShowTimes(keyword, page, size, sortBy, direction))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> handleDetailShowTime(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(showTimeAdminService.getDetailShowTime(id))
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<?> handleCreateShowTime(@Valid @RequestBody ShowTimeRequest showTimeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseWrapper.builder()
                        .status(HttpStatus.CREATED)
                        .code(201)
                        .data(showTimeAdminService.createShowTime(showTimeRequest))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> handleUpdateShowTime(@PathVariable Long id, @Valid @RequestBody ShowTimeRequest showTimeRequest) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(showTimeAdminService.updateShowTime(id, showTimeRequest))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> handleDeleteShowTime(@PathVariable Long id) {
        showTimeAdminService.deleteShowTime(id);
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                .data("Showtime deleted successfully")
                        .build()
        );
    }
}
