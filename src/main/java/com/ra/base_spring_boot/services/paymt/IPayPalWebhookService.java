package com.ra.base_spring_boot.services.paymt;

import java.util.Map;

public interface IPayPalWebhookService {
    void handleWebhook(Map<String, String> headers, Map<String, Object> body);
}
