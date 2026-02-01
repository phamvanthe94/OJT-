package com.ra.base_spring_boot.dto.resp.homeresp;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieTrailerResponse {
    private Long movieId;
    private String title;
    private String trailer;
}
