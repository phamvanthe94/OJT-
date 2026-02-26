package com.ra.base_spring_boot.services.paymt;

import com.ra.base_spring_boot.dto.resp.PayPalCreateOrderResponse;
import com.ra.base_spring_boot.dto.resp.payment.PayPalPaymentResultResponse;

public interface IPaymentService {

    PayPalCreateOrderResponse createPayPalOrder(Long bookingId);

    PayPalPaymentResultResponse captureFromReturn(String orderId);
}