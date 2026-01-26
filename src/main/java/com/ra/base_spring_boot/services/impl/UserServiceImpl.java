package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.FormChangePassword;
import com.ra.base_spring_boot.dto.req.FormUpdateProfile;
import com.ra.base_spring_boot.exception.HttpBadRequest;
import com.ra.base_spring_boot.model.entity.user.User;
import com.ra.base_spring_boot.repository.IUserRepository;
import com.ra.base_spring_boot.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private User getCurrentUserEntity() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User logoutUser() {
        User user = getCurrentUserEntity();
        return user;
    }

    @Override
    public User updateUser(FormUpdateProfile formUpdateProfile) {
        User user = getCurrentUserEntity();
        user.setFullName(formUpdateProfile.getFullName());
        return userRepository.save(user);
    }

    @Override
    public void changeUserPassword(FormChangePassword formChangePassword) {
        User user = getCurrentUserEntity();
        // check old password
        if(!passwordEncoder.matches(formChangePassword.getOldPassword(), user.getPassword())) {
            throw new HttpBadRequest("Old password is incorrect");
        }
        // check confirm password
        if (!formChangePassword.getNewPassword().equals(formChangePassword.getConfirmPassword())) {
            throw new HttpBadRequest("Confirm password do not match");
        }

        // update new password
        user.setPassword(passwordEncoder.encode(formChangePassword.getNewPassword()));
        userRepository.save(user);
    }
}
