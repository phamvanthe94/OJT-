package com.ra.base_spring_boot.model.entity.theater;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.base_spring_boot.model.base.BaseObject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "screens")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Screen extends BaseObject {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "seat_capacity")
    private Integer seatCapacity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
