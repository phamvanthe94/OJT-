package com.ra.base_spring_boot.model.entity.content;

import com.ra.base_spring_boot.model.base.BaseObject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "festival")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Festival extends BaseObject {

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "image", length = 255)
    private String image;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;
}
