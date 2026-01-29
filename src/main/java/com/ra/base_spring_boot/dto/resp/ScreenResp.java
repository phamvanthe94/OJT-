package com.ra.base_spring_boot.dto.resp;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ScreenResp {

    private String name;

    private Integer seatCapacity;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
