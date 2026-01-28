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
    @NotBlank(message = "Tên thể loại không được để trống")
    @Size(max = 255, message = "Tên thể loại không được vượt quá 255 ký tự")
    private String genreName;
}

