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
        String transactionId = "PAYPAL_" + request.getBookingId();

        String paymentUrl = "https://paypal.com/checkout?tx=" + transactionId;

        return new PaymentInitResponse(paymentUrl, transactionId);
    }

    @Override
    public PaymentVerifyResponse verify(PaymentVerifyRequest request) {
        String tx = request.getTransactionId();

        if (tx == null || tx.isBlank()) {
            return new PaymentVerifyResponse(PaymentStatus.FAILED, "Missing transactionId");
        }

        return new PaymentVerifyResponse(PaymentStatus.COMPLETED, tx);
    }
}
