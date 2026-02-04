package com.ra.base_spring_boot.services.payment.gateway.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInitResponse {
    private String paymentUrl;   // FE redirect
    private String transactionId;
}