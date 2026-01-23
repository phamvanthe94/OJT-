package com.ra.base_spring_boot.model.entity.movie;

import com.ra.base_spring_boot.model.base.BaseObject;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "genre")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Genre extends BaseObject {

    @Column(name = "genre_name", nullable = false, length = 255)
    private String genreName;
}
