package com.ra.base_spring_boot.controller.auth;

import com.ra.base_spring_boot.dto.req.authreq.ForgotPasswordRequest;
import com.ra.base_spring_boot.dto.req.authreq.ResetPasswordRequest;
import com.ra.base_spring_boot.services.authsv.IPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class PasswordController {

    private final IPasswordService passwordService;

    /**
     * @apiNote Handle forgot password request by sending a reset link to the user's email.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        passwordService.forgotPassword(request);
        return ResponseEntity.ok("Link đặt lại mật khẩu đã được gửi đến email của bạn.");
    }

    /**
     * @apiNote Handle password reset using the token sent to the user's email.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordService.resetPassword(request);
        return ResponseEntity.ok("Mật khẩu đã được đặt lại thành công.");
    }

    /**
     * @apiNote Simulate a password reset page for demonstration purposes.
     */
    @GetMapping("/reset-password-page")
    public ResponseEntity<?> resetPasswordPage(@RequestParam String token) {
        return ResponseEntity.ok("Copy token này vào Swagger để reset password: " + token);
    }
}
