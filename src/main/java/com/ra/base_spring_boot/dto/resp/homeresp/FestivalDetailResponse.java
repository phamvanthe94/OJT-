package com.ra.base_spring_boot.dto.resp.homeresp;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FestivalDetailResponse {
    private Long id;
    private String title;
    private String image;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
