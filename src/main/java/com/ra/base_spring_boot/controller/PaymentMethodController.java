package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.model.constants.PaymentMethod;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentMethodController {

    @GetMapping("/methods")
    public ResponseEntity<?> methods() {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(Arrays.asList(PaymentMethod.values()))
                        .build()
        );
    }
}
