package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.resp.PayPalCreateOrderResponse;
import com.ra.base_spring_boot.dto.resp.payment.PayPalPaymentResultResponse;
import com.ra.base_spring_boot.services.paymt.IPayPalWebhookService;
import com.ra.base_spring_boot.services.paymt.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/paypal")
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
    public ResponseEntity<String> cancel() {
        return ResponseEntity.ok("Bạn đã huỷ thanh toán");
    }

    // Ping test (OK)
    @GetMapping("/webhook")
    public ResponseEntity<String> webhookPing() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestHeader Map<String, String> headers,
                                          @RequestBody Map<String, Object> body) {
        try {
            webhookService.handleWebhook(headers, body);
            return ResponseEntity.ok("OK");
        } catch (IllegalArgumentException ex) {
            // verify fail / thiếu header => 400
            System.err.println("[PayPalWebhook] BAD REQUEST: " + ex.getMessage());
            return ResponseEntity.badRequest().body("BAD_REQUEST");
        } catch (Exception ex) {
            // lỗi hệ thống => 500 (PayPal sẽ retry)
            System.err.println("[PayPalWebhook] SERVER ERROR: " + ex.getMessage());
            return ResponseEntity.internalServerError().body("SERVER_ERROR");
        }
    }
}