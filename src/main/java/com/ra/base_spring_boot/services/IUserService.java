package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.req.FormChangePassword;
import com.ra.base_spring_boot.dto.req.FormUpdateProfile;
import com.ra.base_spring_boot.model.entity.user.User;

public interface IUserService {
    User logoutUser();

    User updateUser(FormUpdateProfile formUpdateProfile);

    void changeUserPassword(FormChangePassword formChangePassword);
}
