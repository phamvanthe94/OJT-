package com.ra.base_spring_boot.services.payment.gateway.impl;

import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.services.payment.gateway.PaymentGateway;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentInitRequest;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentInitResponse;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentVerifyRequest;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentVerifyResponse;
import org.springframework.stereotype.Component;

@Component("vnpayGateway")
public class VnpayGateway implements PaymentGateway {

    @Override
    public PaymentInitResponse init(PaymentInitRequest request) {
        String transactionId = "VNPAY_" + request.getBookingId();

        String paymentUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?txnRef=" + transactionId;

        return new PaymentInitResponse(paymentUrl, transactionId);
    }

    @Override
    public PaymentVerifyResponse verify(PaymentVerifyRequest request) {
        if (request.isSuccess()) {
            return new PaymentVerifyResponse(PaymentStatus.COMPLETED, "VNPay: payment completed successfully");
        }
        return new PaymentVerifyResponse(PaymentStatus.FAILED, "VNPay: payment failed");
    }
}
