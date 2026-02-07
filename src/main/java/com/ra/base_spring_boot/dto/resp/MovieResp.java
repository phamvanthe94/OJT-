package com.ra.base_spring_boot.dto.resp;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MovieResp {
    private Long id;
    private String title;
    private String descriptions;
    private String author;
    private String image;
    private String trailer;
    private String type;      // trả string cho FE dễ dùng
    private Integer duration;
    private LocalDate releaseDate;
    private String status;    // trả string
    private Set<String> genres;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
