package com.ra.base_spring_boot.services.paymt.impl;

import com.ra.base_spring_boot.exception.TechnicalException;
import com.ra.base_spring_boot.services.paymt.IEmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:no-reply@example.com}")
    private String fromEmail;

    @Value("${app.mail.from-name}")
    private String fromName;

    @Override
    public void sendTicketMailWithQrPng(
            String to,
            String subject,
            String htmlBody,
            byte[] qrPngBytes,
            String fileName
    ) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage, true, StandardCharsets.UTF_8.name()
            );

            setFromSafe(helper);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            if (qrPngBytes != null && qrPngBytes.length > 0) {
                helper.addAttachment(
                        (fileName != null && !fileName.isBlank()) ? fileName : "ticket_qr.png",
                        new ByteArrayResource(qrPngBytes),
                        "image/png"
                );
            }

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new TechnicalException("Failed to send email with attachment", e);
        }
    }

    @Override
    public void sendTicketMailInlineQr(
            String to,
            String subject,
            String htmlBody,
            byte[] qrPngBytes,
            String inlineImageName
    ) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage, true, StandardCharsets.UTF_8.name()
            );

            setFromSafe(helper);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            if (qrPngBytes != null && qrPngBytes.length > 0) {
                String cid = (inlineImageName != null && !inlineImageName.isBlank())
                        ? inlineImageName
                        : "qrImage";

                helper.addInline(cid, new ByteArrayResource(qrPngBytes), "image/png");
            }

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new TechnicalException("Failed to send email with inline QR", e);
        }
    }

    private void setFromSafe(MimeMessageHelper helper) {
        try {
            helper.setFrom(fromEmail, fromName);
        } catch (Exception ex) {
            try {
                helper.setFrom(fromEmail);
            } catch (Exception innerEx) {
                log.warn("Failed to set mail from address: {}", innerEx.getMessage());
            }
        }
    }
}
