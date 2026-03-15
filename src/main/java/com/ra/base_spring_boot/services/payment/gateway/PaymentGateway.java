package com.ra.base_spring_boot.services.payment.gateway;

import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentInitRequest;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentInitResponse;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentVerifyRequest;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentVerifyResponse;

public interface PaymentGateway {

    PaymentInitResponse init(PaymentInitRequest request);

    PaymentVerifyResponse verify(PaymentVerifyRequest request);
}
