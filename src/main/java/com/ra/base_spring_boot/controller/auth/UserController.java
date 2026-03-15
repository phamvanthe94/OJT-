package com.ra.base_spring_boot.controller.auth;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.authreq.FormChangePassword;
import com.ra.base_spring_boot.dto.req.authreq.FormUpdateProfile;
import com.ra.base_spring_boot.services.authsv.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor

public class UserController {

    private final IUserService userService;


    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> handleUpdateUser(@Valid @ModelAttribute FormUpdateProfile formUpdateProfile) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(userService.updateUser(formUpdateProfile))
                        .build()
        );
    }

    @PutMapping("/password")
    public ResponseEntity<?> handleChangePassword(@Valid @RequestBody FormChangePassword formChangePassword) {
        userService.changeUserPassword(formChangePassword);
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                .data("Password changed successfully")
                        .build()
        );
    }
}
