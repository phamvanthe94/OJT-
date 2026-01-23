package com.ra.base_spring_boot.model.entity.theater;

import com.ra.base_spring_boot.model.base.BaseObject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "theaters")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Theater extends BaseObject {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "location", nullable = false, length = 255)
    private String location;

    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;
}
