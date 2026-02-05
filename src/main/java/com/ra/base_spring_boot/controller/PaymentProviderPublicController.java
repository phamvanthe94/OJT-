package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.services.IPaymentProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment-providers")
@RequiredArgsConstructor
public class PaymentProviderPublicController {

    private final IPaymentProviderService paymentProviderService;

    @GetMapping
    public ResponseEntity<?> getAllProviders() {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(paymentProviderService.findAll())
                        .build()
        );
    }
}