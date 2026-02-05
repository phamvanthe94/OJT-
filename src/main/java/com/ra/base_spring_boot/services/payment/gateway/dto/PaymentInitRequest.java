
package com.ra.base_spring_boot.services.payment.gateway.dto;

import com.ra.base_spring_boot.model.constants.PaymentMethod;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInitRequest {
    private Long bookingId;
    private Long amount;
    private PaymentMethod method; // PAYPAL / VNPAY / STRIPE
}