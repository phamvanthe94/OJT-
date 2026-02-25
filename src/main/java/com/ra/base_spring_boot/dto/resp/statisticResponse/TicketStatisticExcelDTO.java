package com.ra.base_spring_boot.dto.resp.statisticResponse;

import com.ra.base_spring_boot.model.constants.SeatType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketStatisticExcelDTO {
    private String movieTitle;
    private SeatType seatType;
    private Double price;
    private Long quantity;
    private Double totalAmount;
}
