
package com.ra.base_spring_boot.services.payment.gateway.dto;

import com.ra.base_spring_boot.model.constants.PaymentMethod;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentVerifyRequest {
    private String transactionId;
    private boolean success;
    private PaymentMethod method;
}
