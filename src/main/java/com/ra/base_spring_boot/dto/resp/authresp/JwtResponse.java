package com.ra.base_spring_boot.dto.resp.authresp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JwtResponse {
    private final String type = "Bearer";
    private String accessToken;
    private UserInfoResponse user;

    private Set<String> roles;
}
