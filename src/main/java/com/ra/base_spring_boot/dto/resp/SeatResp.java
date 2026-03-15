package com.ra.base_spring_boot.dto.resp;

import com.ra.base_spring_boot.model.constants.SeatType;
import com.ra.base_spring_boot.model.entity.theater.Screen;
import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SeatResp {

    private String seatNumber;

    private Boolean isVariable = false;

    private SeatType type;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
