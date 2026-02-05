package com.ra.base_spring_boot.dto.resp;

import com.ra.base_spring_boot.model.entity.theater.ShowTime;
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

    public static ShowTimeListResponse fromEntity(ShowTime s) {
        return ShowTimeListResponse.builder()
                .id(s.getId())

                .movieId(s.getMovie().getId())
                .movieTitle(s.getMovie().getTitle())

                .screenId(s.getScreen().getId())
                .screenName(s.getScreen().getName())

                .theaterId(s.getScreen().getTheater().getId())
                .theaterName(s.getScreen().getTheater().getName())

                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .build();
    }

}

