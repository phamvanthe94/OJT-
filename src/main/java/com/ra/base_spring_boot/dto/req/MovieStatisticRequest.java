package com.ra.base_spring_boot.dto.req;

import lombok.*;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class MovieStatisticRequest {
    private LocalDate fromDate; // Từ ngày
    private LocalDate toDate;   // Đến ngày
}
