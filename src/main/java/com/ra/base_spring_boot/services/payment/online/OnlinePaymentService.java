package com.ra.base_spring_boot.services.payment.online;

import com.ra.base_spring_boot.model.constants.PaymentMethod;
import com.ra.base_spring_boot.services.payment.gateway.PaymentGateway;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentInitRequest;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentInitResponse;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentVerifyRequest;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentVerifyResponse;
import com.ra.base_spring_boot.services.payment.gateway.factory.PaymentGatewayFactory;
import org.springframework.stereotype.Service;


@Service
public class OnlinePaymentService {

    private final PaymentGatewayFactory factory;

    public OnlinePaymentService(PaymentGatewayFactory factory) {
        this.factory = factory;
    }

    public PaymentInitResponse initPayment(Long bookingId, Long amount, PaymentMethod method) {
        PaymentGateway gateway = factory.getGateway(method);

        PaymentInitRequest req = new PaymentInitRequest();
        req.setBookingId(bookingId);
        req.setAmount(amount);
        req.setMethod(method);

        return gateway.init(req);
    }

    public PaymentVerifyResponse verifyPayment(PaymentMethod method, String transactionId, boolean success) {
        PaymentGateway gateway = factory.getGateway(method);

        PaymentVerifyRequest req = new PaymentVerifyRequest();
        req.setMethod(method);
        req.setTransactionId(transactionId);
        req.setSuccess(success);

        return gateway.verify(req);
    }
}
