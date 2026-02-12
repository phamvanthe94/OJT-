package com.ra.base_spring_boot.services.authsv;

import com.ra.base_spring_boot.dto.req.authreq.FormChangePassword;
import com.ra.base_spring_boot.dto.req.authreq.FormUpdateProfile;
import com.ra.base_spring_boot.dto.resp.authresp.UserProfileResponse;

public interface IUserService {

    UserProfileResponse updateUser(FormUpdateProfile formUpdateProfile);

    void changeUserPassword(FormChangePassword formChangePassword);

}
