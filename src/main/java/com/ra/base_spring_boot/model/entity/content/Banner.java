package com.ra.base_spring_boot.model.entity.content;

import com.ra.base_spring_boot.model.base.BaseObject;
import com.ra.base_spring_boot.model.constants.BannerType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "banners")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Banner extends BaseObject {

    @Column(name = "url", nullable = false, length = 255)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private BannerType type;

    @Column(name = "position", nullable = false, length = 255)
    private String position;
}
