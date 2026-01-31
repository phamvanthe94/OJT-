package com.ra.base_spring_boot.dto.resp;

import lombok.*;

import java.time.LocalDate;

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
    private LocalDate releaseDate;
}
