package com.ra.base_spring_boot.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormUpdateProfile {

    @NotBlank(message = "Không được để trống")
    private String firstName;

    @NotBlank(message = "Không được để trống")
    private String lastName;


    private String phone;
    private String address;
    private String avatar;
}
