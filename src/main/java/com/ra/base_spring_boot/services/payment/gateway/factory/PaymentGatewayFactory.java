package com.ra.base_spring_boot.services.payment.gateway.factory;

import com.ra.base_spring_boot.model.constants.PaymentMethod;
import com.ra.base_spring_boot.services.payment.gateway.PaymentGateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayFactory {

    private final PaymentGateway paypalGateway;
    private final PaymentGateway vnpayGateway;
    private final PaymentGateway stripeGateway;

    public PaymentGatewayFactory(
            @Qualifier("paypalGateway") PaymentGateway paypalGateway,
            @Qualifier("vnpayGateway") PaymentGateway vnpayGateway,
            @Qualifier("stripeGateway") PaymentGateway stripeGateway
    ) {
        this.paypalGateway = paypalGateway;
        this.vnpayGateway = vnpayGateway;
        this.stripeGateway = stripeGateway;
    }

    public PaymentGateway getGateway(PaymentMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("PaymentMethod is null");
        }

        return switch (method) {
            case PAYPAL -> paypalGateway;
            case VNPAY -> vnpayGateway;
            case STRIPE -> stripeGateway;
            default -> throw new IllegalArgumentException("Unsupported PaymentMethod: " + method);
        };
    }
}
