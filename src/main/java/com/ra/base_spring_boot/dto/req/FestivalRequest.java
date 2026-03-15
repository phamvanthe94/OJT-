package com.ra.base_spring_boot.dto.req;

import com.ra.base_spring_boot.validation.ValidFestivalTime;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ValidFestivalTime
public class FestivalRequest {
    @NotBlank(message = "Title cannot be blank")
    private String title;
    private MultipartFile image;

    @NotNull
    @FutureOrPresent(message = "Start time must be today or a future date")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime endTime;
}

