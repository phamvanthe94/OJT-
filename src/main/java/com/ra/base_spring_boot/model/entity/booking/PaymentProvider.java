package com.ra.base_spring_boot.model.entity.booking;

import com.ra.base_spring_boot.model.base.BaseObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentProvider extends BaseObject {
    @Column(name = "provider_name", nullable = false, unique = true)
    private String providerName;

    @Column(name = "provider_code", nullable = false, unique = true)
    private String providerCode;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "description")
    private String description;
}
