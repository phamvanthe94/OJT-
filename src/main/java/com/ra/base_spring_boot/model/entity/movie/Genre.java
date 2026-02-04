package com.ra.base_spring_boot.model.entity.movie;

import com.ra.base_spring_boot.model.base.BaseObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "genres")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Genre extends BaseObject {

    @Column(name = "genre_name", nullable = false, length = 255)
    private String genreName;
}
