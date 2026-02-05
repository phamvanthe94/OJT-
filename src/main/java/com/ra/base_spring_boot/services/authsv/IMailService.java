package com.ra.base_spring_boot.services.authsv;

public interface IMailService {
    void sendResetPasswordLink(String toEmail, String resetLink);
}
