package com.ra.base_spring_boot.services.payment.gateway.impl;

import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.services.payment.gateway.PaymentGateway;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentInitRequest;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentInitResponse;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentVerifyRequest;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentVerifyResponse;
import org.springframework.stereotype.Component;

@Component("stripeGateway")
public class StripeGateway implements PaymentGateway {

    @Override
    public PaymentInitResponse init(PaymentInitRequest request) {
        String transactionId = "STRIPE_" + request.getBookingId();

        String paymentUrl = "https://checkout.stripe.com/pay/" + transactionId;

        return new PaymentInitResponse(paymentUrl, transactionId);
    }

    @Override
    public PaymentVerifyResponse verify(PaymentVerifyRequest request) {
        if (request.isSuccess()) {
            return new PaymentVerifyResponse(PaymentStatus.COMPLETED, "Stripe: payment completed successfully");
        }
        return new PaymentVerifyResponse(PaymentStatus.FAILED, "Stripe: payment failed");
    }
}
