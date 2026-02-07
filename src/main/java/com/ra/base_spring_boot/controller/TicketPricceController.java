package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.TicketPriceDTO;
import com.ra.base_spring_boot.model.entity.booking.TicketPrice;
import com.ra.base_spring_boot.services.impl.TicketPriceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ticket-prices")
public class TicketPricceController {
    @Autowired
    private TicketPriceService ticketPriceService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<Page<TicketPrice>>> getAllTicketPrice(@RequestParam(name = "dayType", required = false) Boolean dayType,
                                                                                @RequestParam(name = "seatType", required = false) String seatType,
                                                                                @RequestParam(name = "movieType", required = false) String movieType,
                                                                                @RequestParam(name = "page", defaultValue = "0") int page,
                                                                                @RequestParam(name = "size", defaultValue = "5") int size,
                                                                                @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
                                                                                @RequestParam(name = "direction", defaultValue = "desc") String direction) {
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return ticketPriceService.getAllAndSearchTicketPrice(dayType, seatType, movieType, pageRequest);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseWrapper<?>> createTicketPrice(@RequestBody @Valid TicketPriceDTO ticketPriceDTO, BindingResult bindingResult) {
        ResponseEntity<ResponseWrapper<?>> responseEntity = ticketPriceService.createTicketPrice(ticketPriceDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    ResponseWrapper
                            .<Object>builder()
                            .data(bindingResult.getAllErrors())
                            .code(400)
                            .status(org.springframework.http.HttpStatus.BAD_REQUEST)
                            .build()
            );
        }
        return responseEntity;

    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseWrapper<?>> updateTicketPrice(@PathVariable Long id, @RequestBody @Valid TicketPriceDTO ticketPriceDTO) {
        return ticketPriceService.updateTicketPrice(id, ticketPriceDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteTicketPrice(@PathVariable Long id) {
        return ticketPriceService.deleteTicketPrice(id);
    }
}
