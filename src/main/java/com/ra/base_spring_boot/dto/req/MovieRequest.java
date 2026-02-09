package com.ra.base_spring_boot.dto.req;

import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.constants.MovieType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate releaseDate;

    @NotNull
    private MovieStatus status;

    // FE gửi list id genre
    private Set<Long> genreIds;
}
