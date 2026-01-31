package com.ra.base_spring_boot.dto.resp.statisticResponse;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketByScreenResponse {
    private Long screenId;
    private String screenName;
    private Long totalTicket;
}
