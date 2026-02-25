package com.ra.base_spring_boot.dto.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookingRequest {

    @NotNull
    private Long showTimeId;

    @NotEmpty
    private List<Long> seatIds;
}
