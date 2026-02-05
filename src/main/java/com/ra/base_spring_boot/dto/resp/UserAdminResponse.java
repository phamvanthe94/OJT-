package com.ra.base_spring_boot.dto.resp;

import com.ra.base_spring_boot.model.constants.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserAdminResponse {

    private Long id;

    private String firstName;
    private String lastName;

    private String email;

    private UserStatus status;

    private Set<String> roles;
}
