package com.ra.base_spring_boot.dto.resp;

import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.constants.MovieType;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MovieResponse {
    private Long id;
    private String title;
    private String descriptions;
    private String author;
    private String image;
    private String trailer;
    private MovieType type;
    private Integer duration;
    private LocalDate releaseDate;
    private MovieStatus status;
    private Set<String> genres;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
