package com.ra.base_spring_boot.dto.statistic.resp;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class MovieStatisticResponse {
    private long comingSoon;
    private long nowShowing;
    private long offline;
}
