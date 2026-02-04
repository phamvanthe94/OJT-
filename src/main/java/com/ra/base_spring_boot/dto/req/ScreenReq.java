package com.ra.base_spring_boot.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.base_spring_boot.model.entity.theater.Theater;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ScreenReq {

    private String name;

    private Integer seatCapacity;

}
