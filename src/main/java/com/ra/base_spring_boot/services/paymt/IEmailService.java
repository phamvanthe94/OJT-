package com.ra.base_spring_boot.services.paymt;

public interface IEmailService {


    void sendTicketMailWithQrPng(
            String to,
            String subject,
            String htmlBody,
            byte[] qrPngBytes,
            String fileName
    );

 
    void sendTicketMailInlineQr(
            String to,
            String subject,
            String htmlBody,
            byte[] qrPngBytes,
            String inlineImageName
    );
}
