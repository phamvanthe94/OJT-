package com.ra.base_spring_boot.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormUpdateProfile {
    @NotBlank(message = "Không được để trống")
    private String fullName;
}
