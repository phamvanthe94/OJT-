package com.ra.base_spring_boot.dto.resp.homeresp;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MovieListResponse {
    private Long id;
    private String title;
    private String image;
    private Integer duration;
    private LocalDateTime releaseDate;
}
