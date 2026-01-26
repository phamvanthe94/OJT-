package com.ra.base_spring_boot.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BannerDTO {
    private MultipartFile url;
    private String type;
    @NotBlank(message = "Position cannot be blank")
    private String position;
}
