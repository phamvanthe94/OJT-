package com.ra.base_spring_boot.dto.req;

import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.constants.MovieType;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MovieReq {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @Size(max = 5000, message = "Descriptions must be at most 5000 characters")
    private String descriptions;

    @Size(max = 100, message = "Author must be at most 100 characters")
    private String author;

    @Size(max = 255, message = "Trailer must be at most 255 characters")
    private String trailer;

    @NotNull(message = "Type is required")
    private MovieType type;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 500, message = "Duration must be at most 500 minutes")
    private Integer duration;

    @NotNull(message = "ReleaseDate is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @NotNull(message = "Status is required")
    private MovieStatus status;

    private Set<@NotNull(message = "GenreId is invalid") Long> genreIds;

    private MultipartFile image;
}
