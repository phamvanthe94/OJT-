package com.ra.base_spring_boot.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final java.util.Optional<JavaMailSender> mailSender;


    public void sendBookingSuccessMail(
            String to,
            Long bookingId,
            Long amount,
            String paymentMethod
    ) {
        if (mailSender == null || mailSender.isEmpty()) {
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Xác nhận đặt vé thành công 🎬");

        message.setText(
                "🎉 ĐẶT VÉ THÀNH CÔNG 🎉\n\n" +
                        "Mã booking: " + bookingId + "\n" +
                        "Phương thức thanh toán: " + paymentMethod + "\n" +
                        "Tổng tiền: " + amount + " VND\n\n" +
                        "Chúc bạn xem phim vui vẻ ❤️"
        );

        mailSender.get().send(message);
    }
}