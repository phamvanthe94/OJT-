package com.ra.base_spring_boot.dto.req;

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

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;
}
