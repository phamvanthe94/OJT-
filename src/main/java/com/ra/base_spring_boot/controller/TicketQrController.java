package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.resp.TicketQrData;
import com.ra.base_spring_boot.services.more.TicketQrService;
import com.ra.base_spring_boot.services.paymt.IQrCodeService;
import com.ra.base_spring_boot.services.paymt.ITicketQrTextBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tickets")
public class TicketQrController {

    private final TicketQrService ticketQrService;
    private final IQrCodeService qrCodeService;

    private final ITicketQrTextBuilder qrTextBuilder;

    @GetMapping("/{bookingId}/qr-data")
    public ResponseEntity<?> getQrData(@PathVariable Long bookingId) {
        List<TicketQrData> list = ticketQrService.getQrData(bookingId);

        if (list == null || list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseWrapper.builder()
                            .status(HttpStatus.NOT_FOUND)
                            .code(404)
                .data("Booking not found")
                            .build()
            );
        }

        return ResponseEntity.ok(
                ResponseWrapper.<List<TicketQrData>>builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(list)
                        .build()
        );
    }

    @GetMapping("/{bookingId}/qr")
    public ResponseEntity<?> generateQr(@PathVariable Long bookingId) {

        List<TicketQrData> list = ticketQrService.getQrData(bookingId);

        if (list == null || list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseWrapper.builder()
                            .status(HttpStatus.NOT_FOUND)
                            .code(404)
                .data("No data found to generate QR code")
                            .build()
            );
        }

        String qrText = qrTextBuilder.buildQrText(list);
        byte[] qrImage = qrCodeService.generateQrCode(qrText, 320, 320);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(qrImage);
    }
}
