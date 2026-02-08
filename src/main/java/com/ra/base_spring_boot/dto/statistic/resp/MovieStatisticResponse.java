package com.ra.base_spring_boot.dto.statistic.resp;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class MovieStatisticResponse {
    private long released;     // Đã chiếu
    private long nowShowing;   // Đang chiếu
    private long upcoming;     // Sắp chiếu
}
