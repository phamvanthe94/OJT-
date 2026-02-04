package com.ra.base_spring_boot.dto.resp.TheaterResp;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)

public class TheaterResp {
    private String name;

    private String location;

    private String phone;

}
