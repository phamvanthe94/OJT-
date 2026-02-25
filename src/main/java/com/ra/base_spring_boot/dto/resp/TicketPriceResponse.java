package com.ra.base_spring_boot.dto.resp;
import lombok.*;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TicketPriceResponse {

    private Long id;
    private String typeSeat;
    private String typeMovie;
    private Double price;
    private Boolean dayType;
    private LocalTime startTime;
    private LocalTime endTime;
}
