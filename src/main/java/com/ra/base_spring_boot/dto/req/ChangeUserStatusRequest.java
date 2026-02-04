package com.ra.base_spring_boot.dto.req;

import com.ra.base_spring_boot.model.constants.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChangeUserStatusRequest {

    @NotNull(message = "Status không được để trống")
    private UserStatus status; // ACTIVE / BLOCKED
}
