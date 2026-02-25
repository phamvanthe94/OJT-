package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.TicketPriceRequest;
import com.ra.base_spring_boot.services.ITicketPriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ticket-prices")
@RequiredArgsConstructor
public class TicketPriceController {

    private final ITicketPriceService ticketPriceService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) Boolean dayType,
            @RequestParam(required = false) String seatType,
            @RequestParam(required = false) String movieType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(ticketPriceService.getAllAndSearchTicketPrice(
                                dayType, seatType, movieType,
                                page, size, sortBy, direction
                        ))
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody TicketPriceRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseWrapper.builder()
                        .status(HttpStatus.CREATED)
                        .code(201)
                        .data(ticketPriceService.createTicketPrice(request))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody TicketPriceRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(ticketPriceService.updateTicketPrice(id, request))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ticketPriceService.deleteTicketPrice(id);
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data("Delete ticket price successfully")
                        .build()
        );
    }
}