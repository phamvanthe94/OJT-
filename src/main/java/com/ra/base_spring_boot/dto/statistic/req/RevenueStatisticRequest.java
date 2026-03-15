package com.ra.base_spring_boot.dto.statistic.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueStatisticRequest {

    private Long movieId;
    private Long genreId;
    private Long theaterId;
    private Long screenId;
}
