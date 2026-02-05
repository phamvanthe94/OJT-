package com.ra.base_spring_boot.services.authsv;

import com.ra.base_spring_boot.dto.req.ForgotPasswordRequest;
import com.ra.base_spring_boot.dto.req.ResetPasswordRequest;

public interface IPasswordService {
    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}
