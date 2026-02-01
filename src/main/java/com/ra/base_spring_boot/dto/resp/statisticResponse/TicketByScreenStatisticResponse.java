package com.ra.base_spring_boot.dto.resp.statisticResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketByScreenStatisticResponse {
    private List<TicketByScreenResponse> data;
    private Long totalTicketSold;
}
