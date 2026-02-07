package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.services.impl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class MailTestController {

    private final EmailService emailService;

    @GetMapping("/mail")
    public ResponseEntity<?> testMail(@RequestParam String to) {

        // amount có thể null cũng được, tuỳ EmailService của cậu
        emailService.sendBookingSuccessMail(to, 1L, 120000L, "VNPAY");

        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data("Mail sent (if mail config is correct)")
                        .build()
        );
    }
}