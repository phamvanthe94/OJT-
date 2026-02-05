package com.ra.base_spring_boot.services.authsv.impl;

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
            helper.setSubject("Đặt lại mật khẩu");

            String content = "<p>Bạn đã yêu cầu đặt lại mật khẩu.</p>"
                    + "<p>Vui lòng nhấp vào liên kết bên dưới để đặt lại mật khẩu của bạn:</p>"
                    + "<a href=\"" + resetLink + "\">Đặt lại mật khẩu</a>"
                    + "<br><br>"
                    + "<p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>";

            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Gửi email thất bại" + e.getMessage());
        }

    }
}
