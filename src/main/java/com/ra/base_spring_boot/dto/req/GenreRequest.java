package com.ra.base_spring_boot.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenreRequest {
    @NotBlank(message = "Genre name must not be blank")
    @Size(max = 255, message = "Genre name must not exceed 255 characters")
    private String genreName;
}


