package com.ra.base_spring_boot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.base_spring_boot.dto.resp.TicketQrData;
import com.ra.base_spring_boot.services.Theater.QrCodeService;
import com.ra.base_spring_boot.services.more.TicketQrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tickets/")
public class TicketQrController {

    private final TicketQrService ticketQrService;
    private final QrCodeService qrCodeService;
    private final ObjectMapper objectMapper;

    @GetMapping("/{bookingId}/qr-data")
    public ResponseEntity<?> getQrData(@PathVariable Long bookingId) {
        return ResponseEntity.ok(
                ticketQrService.getQrData(bookingId)
        );
    }

    @GetMapping("/{bookingId}/qr")

    public ResponseEntity<byte[]> generateQr(@PathVariable Long bookingId) throws Exception {

        List<TicketQrData> list = ticketQrService.getQrData(bookingId);

        if (list.isEmpty()) {
            throw new RuntimeException("No data to generate QR");
        }

        TicketQrData first = list.get(0);

        String seats = list.stream()
                .map(TicketQrData::getSeatNumber)
                .distinct()
                .collect(Collectors.joining(", "));

        String qrText =
                "Booking ID: " + first.getBookingId() + "\n" +
                        "Khách hàng: " + first.getFullName() + "\n" +
                        "Phim: " + first.getTitle() + "\n" +
                        "Phòng chiếu: " + first.getScreenName() + "\n" +
                        "Giờ chiếu: " + first.getStartTime() + "\n" +
                        "Ghế: " + seats;

        byte[] qrImage = qrCodeService.generateQrCode(qrText, 300, 300);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                .body(qrImage);
    }

}

