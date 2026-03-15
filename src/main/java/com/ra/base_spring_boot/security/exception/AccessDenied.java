package com.ra.base_spring_boot.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.base_spring_boot.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Slf4j
@Component
public class AccessDenied implements AccessDeniedHandler
{
    private final ObjectMapper objectMapper;

    public AccessDenied(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException
    {
        log.warn("Forbidden request: {}", accessDeniedException.getMessage());
        response.setHeader("error", "FORBIDDEN");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        objectMapper.writeValue(response.getOutputStream(), ErrorResponse.builder()
                .success(false)
                .code(HttpStatus.FORBIDDEN.value())
                .status(HttpStatus.FORBIDDEN.name())
                .message("Access is denied")
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build());
    }
}
