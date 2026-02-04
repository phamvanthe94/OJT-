package com.ra.base_spring_boot.dto.resp;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenreResponse {
    private Long id;
    private String genreName;
}
