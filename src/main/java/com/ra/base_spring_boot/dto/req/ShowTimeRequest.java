package com.ra.base_spring_boot.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ShowTimeRequest {
    @NotNull
    private Long screenId;

    @NotNull
    private Long movieId;


    @Schema(example = "2026-03-10 10:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm[:ss]")
    private LocalDateTime startTime;

    @Schema(example = "2026-03-10 12:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm[:ss]")
    private LocalDateTime endTime;
}
