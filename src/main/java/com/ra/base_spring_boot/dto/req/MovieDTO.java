package com.ra.base_spring_boot.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MovieDTO {
    private Long id;
    @NotBlank(message = "Title cannot be blank")
    private String title;
    @NotBlank(message = "Descriptions cannot be blank")
    private String descriptions;
    @NotBlank(message = "Author cannot be blank")
    private String author;
    private MultipartFile image;
    @NotBlank(message = "Trailer cannot be blank")
    private String trailer;
    @NotBlank(message = "Type cannot be blank")
    private String type;
    @NotNull(message = "Duration cannot be null")
    private Integer duration;
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate releaseDate;
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedAt;
}
