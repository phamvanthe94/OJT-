package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.resp.payment.PayPalCreateOrderResponse;
import com.ra.base_spring_boot.dto.resp.payment.PayPalPaymentResultResponse;
import com.ra.base_spring_boot.services.paymt.IPayPalWebhookService;
import com.ra.base_spring_boot.services.paymt.IPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/paypal")
public class PayPalController {

    private final IPaymentService paymentService;
    private final IPayPalWebhookService webhookService;

    @PostMapping("/create")
    public PayPalCreateOrderResponse create(@RequestParam Long bookingId) {
        return paymentService.createPayPalOrder(bookingId);
    }

    @GetMapping("/return")
    public PayPalPaymentResultResponse paypalReturn(@RequestParam("token") String orderId) {
        return paymentService.captureFromReturn(orderId);
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancel(@RequestParam(value = "token", required = false) String orderId) {
        if (orderId != null && !orderId.isBlank()) {
            paymentService.cancelByOrderId(orderId);
        }
        return ResponseEntity.ok("Payment cancelled");
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(
            @RequestHeader Map<String, String> headers,
            @RequestBody Map<String, Object> body
    ) {
        try {
            webhookService.handleWebhook(headers, body);
            return ResponseEntity.ok("OK");
        } catch (IllegalArgumentException ex) {
            log.warn("Invalid PayPal webhook request: {}", ex.getMessage());
            return ResponseEntity.badRequest().body("BAD_REQUEST");
        } catch (Exception ex) {
            log.error("PayPal webhook processing failed", ex);
            return ResponseEntity.internalServerError().body("SERVER_ERROR");
        }
    }
}
