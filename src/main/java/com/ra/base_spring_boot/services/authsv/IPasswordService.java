package com.ra.base_spring_boot.services.authsv;

import com.ra.base_spring_boot.dto.req.authreq.ForgotPasswordRequest;
import com.ra.base_spring_boot.dto.req.authreq.ResetPasswordRequest;

public interface IPasswordService {
    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}
