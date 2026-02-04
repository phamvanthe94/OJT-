package com.ra.base_spring_boot.services.authsv.impl;

import com.ra.base_spring_boot.dto.req.FormChangePassword;
import com.ra.base_spring_boot.dto.req.FormUpdateProfile;
import com.ra.base_spring_boot.exception.HttpBadRequest;
import com.ra.base_spring_boot.model.entity.user.BlacklistedToken;
import com.ra.base_spring_boot.model.entity.user.User;
import com.ra.base_spring_boot.repository.authrp.IBlacklistTokenRepository;
import com.ra.base_spring_boot.repository.authrp.IUserRepository;
import com.ra.base_spring_boot.security.jwt.JwtProvider;
import com.ra.base_spring_boot.services.authsv.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final IBlacklistTokenRepository blacklistTokenRepository;

    private User getCurrentUserEntity() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User updateUser(FormUpdateProfile formUpdateProfile) {
        User user = getCurrentUserEntity();

        user.setFirstName(formUpdateProfile.getFirstName());
        user.setLastName(formUpdateProfile.getLastName());
        user.setPhone(formUpdateProfile.getPhone());
        user.setAddress(formUpdateProfile.getAddress());
        user.setAvatar(formUpdateProfile.getAvatar());

        return userRepository.save(user);
    }

    @Override
    public void changeUserPassword(FormChangePassword formChangePassword) {
        User user = getCurrentUserEntity();

        if (!passwordEncoder.matches(formChangePassword.getOldPassword(), user.getPassword())) {
            throw new HttpBadRequest("Old password is incorrect");
        }

        if (!formChangePassword.getNewPassword().equals(formChangePassword.getConfirmPassword())) {
            throw new HttpBadRequest("Confirm password do not match");
        }

        user.setPassword(passwordEncoder.encode(formChangePassword.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer "))
            return;

        String token = header.substring(7);

        String jti = jwtProvider.extractJti(token);
        Date expiration = jwtProvider.extractExpiration(token);

        if (expiration.before(new Date()))
            return;

        if (!blacklistTokenRepository.existsById(jti)) {
            blacklistTokenRepository.save(
                    BlacklistedToken.builder()
                            .jti(jti)
                            .expiredAt(expiration)
                            .blacklistedAt(new Date())
                            .build()
            );
        }
    }
}
