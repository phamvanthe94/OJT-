package com.ra.base_spring_boot.services.payment.gateway.dto;

import com.ra.base_spring_boot.model.constants.PaymentStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentVerifyResponse {
    private PaymentStatus status;
    private String message;
}