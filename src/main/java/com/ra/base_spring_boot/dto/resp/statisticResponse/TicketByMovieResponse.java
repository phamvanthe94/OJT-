package com.ra.base_spring_boot.dto.resp.statisticResponse;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketByMovieResponse {
    private Long movieId;
    private String movieTitle;
    private Long totalTicket;
}
