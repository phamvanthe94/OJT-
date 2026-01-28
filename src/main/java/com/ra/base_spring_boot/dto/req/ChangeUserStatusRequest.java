package com.ra.base_spring_boot.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChangeUserStatusRequest {
    @NotNull
    private Boolean status; // true = active, false = blocked

}
