package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.FormCreatePaymentProvider;
import com.ra.base_spring_boot.services.IPaymentProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/payment-providers")
@RequiredArgsConstructor
public class PaymentProviderController {

    private final IPaymentProviderService paymentProviderService;

    @GetMapping
    public ResponseEntity<?> getAllPaymentProviders() {
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(paymentProviderService.findAll())
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<?> createPaymentProviders(@Valid @RequestBody FormCreatePaymentProvider form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseWrapper.builder()
                        .status(HttpStatus.CREATED)
                        .code(HttpStatus.CREATED.value())
                        .data(paymentProviderService.create(form))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaymentProvider(@PathVariable Long id) {
        paymentProviderService.delete(id);
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data("Xóa nhà cung cấp thanh toán thành công")
                        .build()
        );
    }
}
