package com.ra.base_spring_boot.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
public record ErrorResponse(
        boolean success,
        int code,
        String status,
        String message,
        String path,
        Instant timestamp,
        Map<String, String> errors
) {
}
