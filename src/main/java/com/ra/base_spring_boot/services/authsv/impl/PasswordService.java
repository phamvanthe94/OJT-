package com.ra.base_spring_boot.services.authsv.impl;

import com.ra.base_spring_boot.dto.req.authreq.ForgotPasswordRequest;
import com.ra.base_spring_boot.dto.req.authreq.ResetPasswordRequest;
import com.ra.base_spring_boot.exception.HttpBadRequest;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.entity.user.PasswordResetToken;
import com.ra.base_spring_boot.model.entity.user.User;
import com.ra.base_spring_boot.repository.authrp.IPasswordResetTokenRepository;
import com.ra.base_spring_boot.repository.authrp.IUserRepository;
import com.ra.base_spring_boot.security.principle.HashUtil;
import com.ra.base_spring_boot.services.authsv.IMailService;
import com.ra.base_spring_boot.services.authsv.IPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordService implements IPasswordService {

    private final IUserRepository userRepository;
    private final IPasswordResetTokenRepository passwordResetTokenRepository;
    private final IMailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${frontend.reset-password-url}")
    private String resetPasswordUrl;

    @Value("${reset-password.expire-minutes:15}")
    private Long expireMinutes;

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new HttpNotFound("User not found for the provided email"));

        String tokenRaw = UUID.randomUUID().toString();
        String tokenHash = HashUtil.sha256(tokenRaw);

        PasswordResetToken tokenEntity = PasswordResetToken.builder()
                .user(user)
                .tokenHash(tokenHash)
                .expiresAt(LocalDateTime.now().plusMinutes(expireMinutes))
                .usedAt(null)
                .build();

        passwordResetTokenRepository.save(tokenEntity);
        mailService.sendResetPasswordLink(user.getEmail(), resetPasswordUrl + "?token=" + tokenRaw);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new HttpBadRequest("Password confirmation does not match");
        }

        String tokenHash = HashUtil.sha256(request.getToken());
        PasswordResetToken tokenEntity = passwordResetTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new HttpBadRequest("Invalid reset token"));

        if (tokenEntity.getUsedAt() != null) {
            throw new HttpBadRequest("Reset token has already been used");
        }
        if (tokenEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new HttpBadRequest("Reset token has expired");
        }

        User user = tokenEntity.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        tokenEntity.setUsedAt(LocalDateTime.now());
        passwordResetTokenRepository.save(tokenEntity);
    }
}
