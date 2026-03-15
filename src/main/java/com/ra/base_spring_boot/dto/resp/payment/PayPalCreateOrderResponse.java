package com.ra.base_spring_boot.dto.resp.payment;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PayPalCreateOrderResponse {

    private Long paymentId;
    private Long bookingId;

    private String orderId;
    private String approveUrl;

    private String status;
    private Double amount;
    private String currency;
}
