package com.ra.base_spring_boot.services.paymt;

import java.util.Map;

public interface IPayPalClientService {

    String getAccessToken();

    Map<String, Object> createOrder(
            String accessToken,
            String returnUrl,
            String cancelUrl,
            String currency,
            String amount,
            String customId
    );

    Map<String, Object> captureOrder(String accessToken, String orderId);

    Map<String, Object> getOrder(String accessToken, String orderId);

    Map<String, Object> verifyWebhookSignature(String accessToken, Map<String, Object> verifyPayload);
}
