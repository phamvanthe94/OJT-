package com.ra.base_spring_boot.dto.resp.homeresp;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class FestivalListResponse {
    private Long id;
    private String title;
    private String image;
    private LocalDate startTime;
    private LocalDate endTime;
}
