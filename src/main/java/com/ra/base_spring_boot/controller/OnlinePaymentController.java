package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentInitRequest;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentInitResponse;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentVerifyRequest;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentVerifyResponse;
import com.ra.base_spring_boot.services.payment.online.OnlinePaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class OnlinePaymentController {

    private final OnlinePaymentService onlinePaymentService;

    @PostMapping("/init")
    public ResponseEntity<PaymentInitResponse> initPayment(
            @RequestBody PaymentInitRequest request
    ) {
        PaymentInitResponse response = onlinePaymentService.initPayment(
                request.getBookingId(),
                request.getAmount(),
                request.getMethod()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<PaymentVerifyResponse> verifyPayment(
            @RequestBody PaymentVerifyRequest request
    ) {
        PaymentVerifyResponse response = onlinePaymentService.verifyPayment(
                request.getMethod(),
                request.getTransactionId(),
                request.isSuccess()
        );
        return ResponseEntity.ok(response);
    }
}
