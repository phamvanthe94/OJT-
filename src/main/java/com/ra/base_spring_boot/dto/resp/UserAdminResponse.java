package com.ra.base_spring_boot.dto.resp;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserAdminResponse {
    private Long id;
    private String fullName;
    private String username;
    private Boolean status;
    private Set<String> roles;
}