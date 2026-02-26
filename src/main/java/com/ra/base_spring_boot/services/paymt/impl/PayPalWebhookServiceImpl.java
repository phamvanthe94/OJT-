package com.ra.base_spring_boot.services.paymt.impl;

import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.booking.Payment;
import com.ra.base_spring_boot.repository.payment.PaymentRepository;
import com.ra.base_spring_boot.services.paymt.IPayPalClientService;
import com.ra.base_spring_boot.services.paymt.IPayPalWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PayPalWebhookServiceImpl implements IPayPalWebhookService {

    private final IPayPalClientService payPalClientService;
    private final PaymentRepository paymentRepository;

    @Value("${paypal.webhook-id}")
    private String webhookId;

    @Override
    public void handleWebhook(Map<String, String> headers, Map<String, Object> body) {
        // 1) Verify signature (FAIL -> throw)
        verifySignature(headers, body);

        // 2) Route theo event_type
        String eventType = str(body.get("event_type"));
        if (eventType == null) {
            throw new IllegalArgumentException("Missing event_type");
        }

        switch (eventType) {
            case "PAYMENT.CAPTURE.COMPLETED" -> onCaptureCompleted(body);
            case "PAYMENT.CAPTURE.DENIED" -> onCaptureDenied(body);
            // (optional) Nếu bạn muốn xử lý thêm:
            // case "CHECKOUT.ORDER.APPROVED" -> onOrderApproved(body);
            default -> {
                // Không xử lý vẫn OK: trả 200 để PayPal khỏi retry vì event không liên quan
                System.out.println("[PayPalWebhook] Ignore event_type=" + eventType);
            }
        }
    }

    private void verifySignature(Map<String, String> headers, Map<String, Object> body) {
        String transmissionId = headerIgnoreCaseOrNull(headers, "PAYPAL-TRANSMISSION-ID");
        String transmissionTime = headerIgnoreCaseOrNull(headers, "PAYPAL-TRANSMISSION-TIME");
        String certUrl = headerIgnoreCaseOrNull(headers, "PAYPAL-CERT-URL");
        String authAlgo = headerIgnoreCaseOrNull(headers, "PAYPAL-AUTH-ALGO");
        String transmissionSig = headerIgnoreCaseOrNull(headers, "PAYPAL-TRANSMISSION-SIG");

        // Thiếu header => webhook không hợp lệ (trả 400 ở controller)
        if (transmissionId == null || transmissionTime == null || certUrl == null
                || authAlgo == null || transmissionSig == null) {
            throw new IllegalArgumentException("Missing PayPal signature headers");
        }

        String token = payPalClientService.getAccessToken();

        Map<String, Object> verifyPayload = Map.of(
                "transmission_id", transmissionId,
                "transmission_time", transmissionTime,
                "cert_url", certUrl,
                "auth_algo", authAlgo,
                "transmission_sig", transmissionSig,
                "webhook_id", webhookId,
                "webhook_event", body
        );

        Map<String, Object> resp = payPalClientService.verifyWebhookSignature(token, verifyPayload);
        String status = resp == null ? null : str(resp.get("verification_status"));

        if (!"SUCCESS".equalsIgnoreCase(status)) {
            throw new IllegalArgumentException("Invalid PayPal webhook signature. verification_status=" + status);
        }
    }

    @SuppressWarnings("unchecked")
    private void onCaptureCompleted(Map<String, Object> body) {
        Map<String, Object> resource = (Map<String, Object>) body.get("resource");
        if (resource == null) {
            System.out.println("[PayPalWebhook] resource is null");
            return;
        }

        String captureId = str(resource.get("id"));
        String captureStatus = str(resource.get("status"));
        String orderId = extractOrderId(resource);

        System.out.println("[PayPalWebhook] CAPTURE.COMPLETED orderId=" + orderId
                + " captureId=" + captureId + " status=" + captureStatus);

        if (orderId == null) return;

        Payment payment = paymentRepository.findByPaypalOrderId(orderId).orElse(null);
        if (payment == null) {
            System.out.println("[PayPalWebhook] Payment not found by paypalOrderId=" + orderId);
            return;
        }

        // idempotent: đã COMPLETED thì bỏ qua
        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) return;

        // Chỉ set COMPLETED khi PayPal báo COMPLETED
        if ("COMPLETED".equalsIgnoreCase(captureStatus)) {
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            payment.setPaymentTime(LocalDateTime.now());
            payment.setPaypalCaptureId(captureId);
            payment.setTransactionId(captureId);
            paymentRepository.save(payment);
        }
    }

    @SuppressWarnings("unchecked")
    private void onCaptureDenied(Map<String, Object> body) {
        Map<String, Object> resource = (Map<String, Object>) body.get("resource");
        if (resource == null) return;

        String orderId = extractOrderId(resource);
        System.out.println("[PayPalWebhook] CAPTURE.DENIED orderId=" + orderId);

        if (orderId == null) return;

        Payment payment = paymentRepository.findByPaypalOrderId(orderId).orElse(null);
        if (payment == null) return;

        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) return;

        payment.setPaymentStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);
    }

    @SuppressWarnings("unchecked")
    private String extractOrderId(Map<String, Object> resource) {
        // PayPal capture event thường có: supplementary_data.related_ids.order_id
        try {
            Map<String, Object> supplementary = (Map<String, Object>) resource.get("supplementary_data");
            Map<String, Object> related = (Map<String, Object>) supplementary.get("related_ids");
            return str(related.get("order_id"));
        } catch (Exception e) {
            return null;
        }
    }

    private String headerIgnoreCaseOrNull(Map<String, String> headers, String key) {
        for (var e : headers.entrySet()) {
            if (e.getKey() != null && e.getKey().equalsIgnoreCase(key)) {
                return e.getValue();
            }
        }
        return null;
    }

    private String str(Object o) {
        return o == null ? null : String.valueOf(o);
    }
}