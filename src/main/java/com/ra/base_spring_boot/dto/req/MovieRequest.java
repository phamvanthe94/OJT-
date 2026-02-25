package com.ra.base_spring_boot.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.constants.MovieType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieRequest {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Descriptions cannot be blank")
    private String descriptions;

    @NotBlank(message = "Author cannot be blank")
    private String author;

    private MultipartFile image;

    @NotBlank(message = "Trailer cannot be blank")
    private String trailer;

    @NotNull(message = "Type cannot be blank")
    private MovieType type;

    @NotNull(message = "Duration cannot be null")
    private Integer duration;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime releaseDate;

    @NotNull(message = "Release start date cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releaseStartDate;

    @NotNull(message = "Release end date cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releaseEndDate;

    @NotNull()
    private MovieStatus status;

    private Set<Long> genreIds;
}
