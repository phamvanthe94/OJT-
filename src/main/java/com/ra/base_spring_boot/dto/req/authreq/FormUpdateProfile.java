package com.ra.base_spring_boot.dto.req.authreq;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormUpdateProfile {

    @Size(max = 50, message = "firstName tối đa 50 ký tự")
    private String firstName;

    @Size(max = 50, message = "lastName tối đa 50 ký tự")
    private String lastName;

    private String phone;

    @Size(max = 255, message = "address tối đa 255 ký tự")
    private String address;


    private MultipartFile avatar;
}
