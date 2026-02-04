package com.ra.base_spring_boot.dto.resp;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ShowTimeDetailResponse {
    private Long id;

    private Long theaterId;
    private String theaterName;
    private String theaterLocation;

    private Long screenId;
    private String screenName;
    private Integer seatCapacity;

    private Long movieId;
    private String movieTitle;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
