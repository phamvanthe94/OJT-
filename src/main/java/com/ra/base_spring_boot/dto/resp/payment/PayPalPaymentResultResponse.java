package com.ra.base_spring_boot.dto.resp.payment;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PayPalPaymentResultResponse {

    private Long paymentId;
    private Long bookingId;

    private String paymentStatus;     // COMPLETED / FAILED / CANCELLED / PENDING
    private String transactionId;     // captureId
    private Double amount;
    private String currency;

    private String message;           // thông báo cho FE
}