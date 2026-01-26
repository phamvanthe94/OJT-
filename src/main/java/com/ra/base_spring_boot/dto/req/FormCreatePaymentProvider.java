package com.ra.base_spring_boot.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormCreatePaymentProvider {
    @NotBlank(message = "Tên nhà cung cấp không được để trống")
    private String providerName;

    @NotBlank(message = "Tên mã nhà cung cấp không được để trống")
    private String providerCode;

    private String description;

}
