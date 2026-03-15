package com.ra.base_spring_boot.services.paymt.impl;

import com.ra.base_spring_boot.dto.resp.TicketQrData;
import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.booking.Booking;
import com.ra.base_spring_boot.model.entity.booking.Payment;
import com.ra.base_spring_boot.services.more.TicketQrService;
import com.ra.base_spring_boot.services.paymt.IEmailService;
import com.ra.base_spring_boot.services.paymt.IQrCodeService;
import com.ra.base_spring_boot.services.paymt.ITicketIssuanceService;
import com.ra.base_spring_boot.services.paymt.ITicketQrTextBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketIssuanceServiceImpl implements ITicketIssuanceService {

    private final IQrCodeService qrCodeService;
    private final IEmailService emailService;

    private final TicketQrService ticketQrService;
    private final ITicketQrTextBuilder qrTextBuilder;

    @Override
    public void issueTicketAfterPaymentCompleted(Payment payment) {

        if (payment == null) return;

        if (payment.getPaymentStatus() != PaymentStatus.COMPLETED) return;

        Booking booking = payment.getBooking();
        if (booking == null || booking.getId() == null) return;

        Long bookingId = booking.getId();

        String to = (booking.getUser() != null) ? booking.getUser().getEmail() : null;
        if (to == null || to.isBlank()) return;

        List<TicketQrData> qrDataList = ticketQrService.getQrData(bookingId);

        String qrContent = qrTextBuilder.buildQrText(qrDataList);

        if (qrContent == null || qrContent.isBlank()) {
            String bookingCode = booking.getBookingCode() != null ? booking.getBookingCode() : String.valueOf(bookingId);
            qrContent = """
                    MOVIE TICKET
                    ----------------------------
                    Booking: %s
                    ----------------------------
                    """.formatted(bookingCode);
        }

        byte[] qrPng = qrCodeService.generateQrCode(qrContent, 360, 360);

        String bookingCode = booking.getBookingCode() != null ? booking.getBookingCode() : String.valueOf(bookingId);
        String subject = "Movie ticket confirmation - " + bookingCode;

        String html = """
                <div style="font-family: Arial, sans-serif; line-height: 1.6;">
                  <h2 style="margin:0 0 12px;">Ticket booking confirmed</h2>

                  <p><b>Booking code:</b> %s</p>
                  <p><b>Payment method:</b> %s</p>
                  <p><b>Total amount:</b> %s</p>
                  <p><b>Transaction ID:</b> %s</p>

                  <p style="margin:16px 0 8px;">
                    Please present this QR code at check-in:
                  </p>

                  <img src="cid:qrImage"
                       style="width:240px;height:240px;border:1px solid #ddd;padding:8px;border-radius:10px;" />

                  <p style="color:#666;font-size:12px;margin-top:10px;">
                    This QR code is displayed directly in the email.
                  </p>
                </div>
                """.formatted(
                bookingCode,
                String.valueOf(payment.getPaymentMethod()),
                String.valueOf(payment.getAmount()),
                String.valueOf(payment.getTransactionId())
        );

        emailService.sendTicketMailInlineQr(
                to,
                subject,
                html,
                qrPng,
                "qrImage"
        );
    }
}
