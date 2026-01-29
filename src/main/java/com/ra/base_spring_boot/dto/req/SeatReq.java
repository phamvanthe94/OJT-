package com.ra.base_spring_boot.dto.req;

import com.ra.base_spring_boot.model.constants.SeatType;
import com.ra.base_spring_boot.model.entity.theater.Screen;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SeatReq {
    private Screen screen;

    private String seatNumber;

    private Boolean isVariable = false;

    private SeatType type;

}
