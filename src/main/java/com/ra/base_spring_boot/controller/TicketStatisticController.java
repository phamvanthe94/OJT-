package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByMovieStatisticResponse;
import com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByScreenStatisticResponse;
import com.ra.base_spring_boot.services.statisticService.impl.TicketStatisticServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistic/ticket")
public class TicketStatisticController {
    @Autowired
    private TicketStatisticServiceImpl ticketStatisticService;
@GetMapping("/movies")
public ResponseEntity<?> statisticByMovie() {

    return ResponseEntity.ok(
            ResponseWrapper.<TicketByMovieStatisticResponse>builder()
                    .code(200)
                    .status(HttpStatus.OK)
                    .data(ticketStatisticService.statisticByMovie())
                    .build()
    );
}
@GetMapping("/screens")
public ResponseEntity<?> statisticByScreen() {
    return ResponseEntity.ok(
            ResponseWrapper.<TicketByScreenStatisticResponse>builder()
                    .code(200)
                    .status(HttpStatus.OK)
                    .data(ticketStatisticService.statisticByScreen())
                    .build()
    );
}

}
