package com.ra.base_spring_boot.dto.resp.homeresp;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MovieDetailResponse {
    private Long id;
    private String title;
    private String description;
    private String author;
    private String image;
    private String trailer;
    private Integer duration;
    private LocalDate releaseDate;
    private String type;
    private String status;
    private Set<String> genres;
}
