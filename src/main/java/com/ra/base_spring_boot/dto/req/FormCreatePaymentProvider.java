package com.ra.base_spring_boot.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormCreatePaymentProvider {
    @NotBlank(message = "Provider name must not be blank")
    private String providerName;

    @NotBlank(message = "Provider code must not be blank")
    private String providerCode;

    private String description;
}
