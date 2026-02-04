package com.ra.base_spring_boot.dto.resp.statisticResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketByMovieStatisticResponse {
    private List<TicketByMovieResponse> data;
    private Long totalTicketSold;
}
