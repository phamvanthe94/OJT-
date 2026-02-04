package com.ra.base_spring_boot.dto.resp.homeresp;

import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.constants.SeatType;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketPriceResponse {
    private Long id;
    private SeatType seatType;
    private MovieType movieType;
    private Double price;
    private Boolean dayType;
    private LocalTime startTime;
    private LocalTime endTime;
}
