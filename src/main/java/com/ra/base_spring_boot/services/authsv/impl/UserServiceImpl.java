package com.ra.base_spring_boot.services.authsv.impl;

import com.ra.base_spring_boot.dto.req.authreq.FormChangePassword;
import com.ra.base_spring_boot.dto.req.authreq.FormUpdateProfile;
import com.ra.base_spring_boot.dto.resp.authresp.UserProfileResponse;
import com.ra.base_spring_boot.exception.HttpBadRequest;
import com.ra.base_spring_boot.model.entity.user.User;
import com.ra.base_spring_boot.repository.authrp.IUserRepository;
import com.ra.base_spring_boot.services.authsv.IUserService;
import com.ra.base_spring_boot.services.more.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;

    private User getCurrentUserEntity() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }

    @Override
    public UserProfileResponse updateUser(FormUpdateProfile formUpdateProfile) {
        User user = getCurrentUserEntity();


        if (formUpdateProfile.getAvatar() != null && !formUpdateProfile.getAvatar().isEmpty()) {
            String avatarUrl = cloudinaryService.upload(formUpdateProfile.getAvatar());
            user.setAvatar(avatarUrl);
        }

        if (formUpdateProfile.getFirstName() != null && !formUpdateProfile.getFirstName().isBlank()) {
            user.setFirstName(formUpdateProfile.getFirstName());
        }
        if (formUpdateProfile.getLastName() != null && !formUpdateProfile.getLastName().isBlank()) {
            user.setLastName(formUpdateProfile.getLastName());
        }
        if (formUpdateProfile.getPhone() != null && !formUpdateProfile.getPhone().isBlank()) {
            user.setPhone(formUpdateProfile.getPhone());
        }
        if (formUpdateProfile.getAddress() != null && !formUpdateProfile.getAddress().isBlank()) {
            user.setAddress(formUpdateProfile.getAddress());
        }


        User saved = userRepository.save(user);

        return UserProfileResponse.builder()
                .id(saved.getId())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .email(saved.getEmail())
                .phone(saved.getPhone())
                .address(saved.getAddress())
                .avatarUrl(saved.getAvatar())
                .build();
    }

    @Override
    public void changeUserPassword(FormChangePassword formChangePassword) {
        User user = getCurrentUserEntity();

        if (!passwordEncoder.matches(formChangePassword.getOldPassword(), user.getPassword())) {
            throw new HttpBadRequest("Old password is incorrect");
        }

        if (!formChangePassword.getNewPassword().equals(formChangePassword.getConfirmPassword())) {
            throw new HttpBadRequest("Password confirmation does not match");
        }

        user.setPassword(passwordEncoder.encode(formChangePassword.getNewPassword()));
        userRepository.save(user);
    }


}

