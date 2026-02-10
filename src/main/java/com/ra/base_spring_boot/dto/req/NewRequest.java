package com.ra.base_spring_boot.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NewRequest {
    @NotBlank(message = "Title not be blank")
    private String title;
    @NotBlank(message = "Content not be blank")
    private String content;
    @NotNull
    private Long festivalId;
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdAt;
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedAt;
}
