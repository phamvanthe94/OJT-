package com.ra.base_spring_boot.controller.auth;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.authreq.ForgotPasswordRequest;
import com.ra.base_spring_boot.dto.req.authreq.ResetPasswordRequest;
import com.ra.base_spring_boot.services.authsv.IPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/password")
@RequiredArgsConstructor
public class PasswordController {

    private final IPasswordService passwordService;

    /**
     * @apiNote Handle forgot password request by sending a reset link to the user's email.
     */
    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        passwordService.forgotPassword(request);
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data("Link đặt lại mật khẩu đã được gửi đến email của bạn !")
                        .build()
        );
    }

    /**
     * @apiNote Handle password reset using the token sent to the user's email.
     */
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordService.resetPassword(request);
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data("Đặt lại mật khẩu thành công !")
                        .build()
        );
    }

    /**
     * @apiNote Simulate a password reset page for demonstration purposes.
     */
    @GetMapping("/reset-token")
    public ResponseEntity<?> resetPasswordPage(@RequestParam String token) {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data("Copy token này vào Swagger để reset password: " + token)
                        .build()
        );
    }
}
