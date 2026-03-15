package com.ra.base_spring_boot.services.authsv.impl;

import com.ra.base_spring_boot.exception.TechnicalException;
import com.ra.base_spring_boot.services.authsv.IMailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements IMailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendResetPasswordLink(String toEmail, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Reset your password");
            helper.setText(buildResetPasswordContent(resetLink), true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new TechnicalException("Failed to send reset password email", e);
        }
    }

    private String buildResetPasswordContent(String resetLink) {
        return "<p>You requested to reset your password.</p>"
                + "<p>Open the link below to set a new password:</p>"
                + "<a href=\"" + resetLink + "\">Reset password</a>"
                + "<br><br>"
                + "<p>If you did not request this, you can safely ignore this email.</p>";
    }
}
