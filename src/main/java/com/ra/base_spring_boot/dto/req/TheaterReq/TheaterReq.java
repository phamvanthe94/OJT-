package com.ra.base_spring_boot.dto.req.TheaterReq;

import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TheaterReq {
    private String name;

    private String location;

    private String phone;

}
