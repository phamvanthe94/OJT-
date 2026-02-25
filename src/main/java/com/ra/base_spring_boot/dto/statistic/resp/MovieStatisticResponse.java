package com.ra.base_spring_boot.dto.statistic.resp;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class MovieStatisticResponse {
    private long comingSoon;   // COMING_SOON
    private long nowShowing;   // NOW_SHOWING
    private long offline;      // OFFLINE
}
