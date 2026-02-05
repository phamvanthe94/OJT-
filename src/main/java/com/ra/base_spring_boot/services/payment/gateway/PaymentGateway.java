package com.ra.base_spring_boot.services.payment.gateway;

import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentInitRequest;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentInitResponse;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentVerifyRequest;
import com.ra.base_spring_boot.services.payment.gateway.dto.PaymentVerifyResponse;

public interface PaymentGateway {

    // Khởi tạo thanh toán (redirect, url, token…)
    PaymentInitResponse init(PaymentInitRequest request);

    // Xác thực kết quả thanh toán
    PaymentVerifyResponse verify(PaymentVerifyRequest request);
}