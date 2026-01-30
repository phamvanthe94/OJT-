package com.ra.base_spring_boot.dto.resp;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ShowTimeListResponse {

    private Long id;

    private Long theaterId;
    private String theaterName;

    private Long screenId;
    private String screenName;

    private Long movieId;
    private String movieTitle;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

