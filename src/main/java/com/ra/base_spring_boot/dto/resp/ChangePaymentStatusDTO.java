package com.ra.base_spring_boot.dto.resp;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePaymentStatusDTO {

    @NotBlank(message = "newStatus cannot be blank")
    private String newStatus; // ví dụ: SUCCESS, FAILED, PENDING...
}

