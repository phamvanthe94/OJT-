package com.ra.base_spring_boot.dto.resp;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PayPalCreateOrderResponse {

    private Long paymentId;       // id payment trong DB
    private Long bookingId;       // booking liên quan

    private String orderId;       // paypalOrderId (token)
    private String approveUrl;    // link PayPal để user approve

    private String status;        // PENDING / CREATED ...
    private Double amount;        // số tiền
    private String currency;      // "USD" (hoặc bạn dùng VND thì đổi)
}