package com.ra.base_spring_boot.services.authsv;

import com.ra.base_spring_boot.dto.req.FormUpdateProfile;
import com.ra.base_spring_boot.dto.req.authreq.FormChangePassword;
import com.ra.base_spring_boot.model.entity.user.User;
import jakarta.servlet.http.HttpServletRequest;

public interface IUserService {

    User updateUser(FormUpdateProfile formUpdateProfile);

    void changeUserPassword(FormChangePassword formChangePassword);

    void logout(HttpServletRequest request);
}
