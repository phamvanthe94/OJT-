package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.services.paymt.IEmailService;
import com.ra.base_spring_boot.services.paymt.IQrCodeService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
@Hidden
public class TestMailController {

    private final IQrCodeService qrCodeService;
    private final IEmailService emailService;

    @PostMapping("/ticket-mail")
    public String testTicketMail(@RequestParam String to) {

        String bookingCode = "BK-TEST-1040";
        String qrContent = "BOOKING_CODE:" + bookingCode
                + "|HELLO:MAILTRAP_TEST"
                + "|AMOUNT:120000"
                + "|SEATS:A1,A2";

        byte[] qrPng = qrCodeService.generateQrCode(qrContent, 360, 360);

        String subject = "Test Ticket Email - " + bookingCode;

        String html = """
                <div style="font-family: Arial, sans-serif; line-height: 1.6;">
                  <h2 style="margin:0 0 12px;">TEST MAIL</h2>

                  <p>Booking code: <b>%s</b></p>

                  <p style="margin:16px 0 8px;">
                    The QR code is displayed directly in this email:
                  </p>

                  <img src="cid:qrImage"
                       style="width:240px;height:240px;border:1px solid #ddd;padding:8px;border-radius:10px;" />

                  <hr style="margin:18px 0;" />
                  <p><b>DEBUG qrContent:</b></p>
                  <pre style="white-space:pre-wrap;background:#f6f6f6;padding:10px;border-radius:8px;">%s</pre>
                </div>
                """.formatted(bookingCode, qrContent);

        emailService.sendTicketMailInlineQr(
                to,
                subject,
                html,
                qrPng,
                "qrImage"
        );

        return "OK - sent inline QR and attachment. Check Mailtrap inbox.";
    }
}
