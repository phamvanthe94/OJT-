package com.ra.base_spring_boot.services.payment.gateway.impl;

import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.services.payment.gateway.PaymentGateway;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentInitRequest;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentInitResponse;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentVerifyRequest;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentVerifyResponse;
import org.springframework.stereotype.Component;


@Component("paypalGateway")
public class PaypalGateway implements PaymentGateway {

    @Override
    public PaymentInitResponse init(PaymentInitRequest request) {
        // ✅ giả lập transactionId theo bookingId
        String transactionId = "PAYPAL_" + request.getBookingId();

        // ✅ giả lập link thanh toán (sau này thay bằng link thật của PayPal)
        String paymentUrl = "https://paypal.com/checkout?tx=" + transactionId;

        return new PaymentInitResponse(paymentUrl, transactionId);
    }

    @Override
    public PaymentVerifyResponse verify(PaymentVerifyRequest request) {
        // ✅ Giả lập verify: nếu có transactionId thì coi như thanh toán thành công
        // (Sau này thay bằng gọi API thật của PayPal)
        String tx = request.getTransactionId();

        // Nếu tx null/blank thì coi là FAILED
        if (tx == null || tx.isBlank()) {
            return new PaymentVerifyResponse(PaymentStatus.FAILED, "Missing transactionId");
        }

        return new PaymentVerifyResponse(PaymentStatus.COMPLETED, tx);
    }
}