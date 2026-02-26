package com.ra.base_spring_boot.services.paymt.impl;

import com.ra.base_spring_boot.services.paymt.IPayPalClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class PayPalClientServiceImpl implements IPayPalClientService {

    private final WebClient webClient;
    private final String clientId;
    private final String clientSecret;

    public PayPalClientServiceImpl(
            @Value("${paypal.base-url}") String baseUrl,
            @Value("${paypal.client-id}") String clientId,
            @Value("${paypal.client-secret}") String clientSecret
    ) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public String getAccessToken() {
        Map<?, ?> resp = webClient.post()
                .uri("/v1/oauth2/token")
                .headers(h -> h.setBasicAuth(clientId, clientSecret))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("grant_type=client_credentials")
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(15))
                .block();

        if (resp == null || resp.get("access_token") == null) {
            throw new RuntimeException("PayPal: cannot get access_token");
        }
        return (String) resp.get("access_token");
    }

    @Override
    public Map<String, Object> createOrder(String accessToken,
                                           String returnUrl,
                                           String cancelUrl,
                                           String currency,
                                           String amount,
                                           String customId) {

        Map<String, Object> payload = Map.of(
                "intent", "CAPTURE",
                "purchase_units", List.of(Map.of(
                        "custom_id", customId,
                        "amount", Map.of("currency_code", currency, "value", amount)
                )),
                "application_context", Map.of(
                        "return_url", returnUrl,
                        "cancel_url", cancelUrl
                )
        );

        return webClient.post()
                .uri("/v2/checkout/orders")
                .headers(h -> h.setBearerAuth(accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(15))
                .block();
    }

    @Override
    public Map<String, Object> captureOrder(String accessToken, String orderId) {
        return webClient.post()
                .uri("/v2/checkout/orders/{id}/capture", orderId)
                .headers(h -> h.setBearerAuth(accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of())
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(15))
                .block();
    }

    @Override
    public Map<String, Object> verifyWebhookSignature(String accessToken, Map<String, Object> verifyPayload) {
        return webClient.post()
                .uri("/v1/notifications/verify-webhook-signature")
                .headers(h -> h.setBearerAuth(accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(verifyPayload)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(15))
                .block();
    }
}