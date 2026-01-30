package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.FormChangePassword;
import com.ra.base_spring_boot.dto.req.FormUpdateProfile;
import com.ra.base_spring_boot.services.IUserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor

public class UserController {

    private final IUserService userService;

    @PostMapping("/logout")
    public ResponseEntity<?> handleLogout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data("Logout successfully")
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<?> handleUpdateUser(@Valid @RequestBody FormUpdateProfile formUpdateProfile) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(userService.updateUser(formUpdateProfile))
                        .build()
        );
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> handleChangePassword(@Valid @RequestBody FormChangePassword formChangePassword) {
        userService.changeUserPassword(formChangePassword);
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data("Change password successfully")
                        .build()
        );
    }


    @GetMapping("/me")
    public ResponseEntity<?> me(@Parameter(hidden = true) Authentication authentication) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(authentication.getName())
                        .build()
        );

    }
}
