package com.ra.base_spring_boot.dto.resp;

import com.ra.base_spring_boot.model.constants.SeatType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatSelectResp {
        private Long seatId;

        private String seatNumber;

        private SeatType type;

        private Boolean booked;
    }
