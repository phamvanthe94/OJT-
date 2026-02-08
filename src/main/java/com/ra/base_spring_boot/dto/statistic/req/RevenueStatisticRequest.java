package com.ra.base_spring_boot.dto.statistic.req;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class RevenueStatisticRequest {

    // MOVIE | GENRE | DATE | MONTH | YEAR
    private String groupBy;

    private LocalDate fromDate;
    private LocalDate toDate;
}

