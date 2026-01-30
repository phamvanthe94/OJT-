package com.ra.base_spring_boot.model.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BlacklistedToken {
    @Id
    @Column(length = 64)
    private String jti;

    @Column(nullable = false)
    private Date expiredAt;

    @Column(nullable = false)
    private Date blacklistedAt;
}
