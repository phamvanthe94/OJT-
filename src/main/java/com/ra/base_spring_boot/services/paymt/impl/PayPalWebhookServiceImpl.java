package com.ra.base_spring_boot.services.paymt.impl;

import com.ra.base_spring_boot.model.constants.BookingStatus;
import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.booking.Booking;
import com.ra.base_spring_boot.model.entity.booking.Payment;
import com.ra.base_spring_boot.repository.booking.IBookingRepository;
import com.ra.base_spring_boot.repository.payment.PaymentRepository;
import com.ra.base_spring_boot.services.paymt.IPayPalClientService;
import com.ra.base_spring_boot.services.paymt.IPayPalWebhookService;
import com.ra.base_spring_boot.services.paymt.ITicketIssuanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class PayPalWebhookServiceImpl implements IPayPalWebhookService {

    private final IPayPalClientService payPalClientService;
    private final PaymentRepository paymentRepository;
    private final IBookingRepository bookingRepository;
    private final ITicketIssuanceService ticketIssuanceService;

    @Value("${paypal.webhook-id}")
    private String webhookId;

    @Value("${paypal.webhook-verify-enabled}")
    private boolean verifyEnabled;

    @Override
    @Transactional
    public void handleWebhook(Map<String, String> headers, Map<String, Object> body) {
        if (verifyEnabled) {
            verifySignature(headers, body);
        } else {
            log.info("PayPal webhook signature verification is disabled");
        }

        String eventType = str(body.get("event_type"));
        if (eventType == null || eventType.isBlank()) {
            throw new IllegalArgumentException("Missing event_type");
        }

        switch (eventType) {
            case "PAYMENT.CAPTURE.COMPLETED" -> onCaptureCompleted(body);
            case "PAYMENT.CAPTURE.DENIED" -> onCaptureDenied(body);
            default -> log.info("Ignoring PayPal webhook event type {}", eventType);
        }
    }

    private void verifySignature(Map<String, String> headers, Map<String, Object> body) {
        String transmissionId = headerAny(headers,
                "PayPal-Transmission-Id", "PAYPAL-TRANSMISSION-ID", "paypal-transmission-id");
        String transmissionTime = headerAny(headers,
                "PayPal-Transmission-Time", "PAYPAL-TRANSMISSION-TIME", "paypal-transmission-time");
        String certUrl = headerAny(headers,
                "PayPal-Cert-Url", "PAYPAL-CERT-URL", "paypal-cert-url");
        String authAlgo = headerAny(headers,
                "PayPal-Auth-Algo", "PAYPAL-AUTH-ALGO", "paypal-auth-algo");
        String transmissionSig = headerAny(headers,
                "PayPal-Transmission-Sig", "PAYPAL-TRANSMISSION-SIG", "paypal-transmission-sig");

        if (transmissionId == null || transmissionTime == null || certUrl == null
                || authAlgo == null || transmissionSig == null) {
            log.warn("Missing PayPal signature headers. keys={}", headers != null ? headers.keySet() : null);
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

        Map<String, Object> response = payPalClientService.verifyWebhookSignature(token, verifyPayload);
        String status = response == null ? null : str(response.get("verification_status"));
        log.info("PayPal webhook verification status={}", status);

        if (!"SUCCESS".equalsIgnoreCase(status)) {
            log.warn("PayPal webhook signature verification failed. response={}", response);
            throw new IllegalArgumentException("Invalid PayPal webhook signature. status=" + status);
        }
    }

    @SuppressWarnings("unchecked")
    private void onCaptureCompleted(Map<String, Object> body) {
        Map<String, Object> resource = (Map<String, Object>) body.get("resource");
        if (resource == null) {
            log.warn("PayPal capture completed webhook is missing resource");
            return;
        }

        String captureId = str(resource.get("id"));
        String captureStatus = str(resource.get("status"));
        String orderId = extractOrderId(resource);
        log.info("PayPal capture completed. orderId={}, captureId={}, status={}", orderId, captureId, captureStatus);

        if (orderId == null || orderId.isBlank()) {
            return;
        }

        Payment payment = paymentRepository.findByPaypalOrderId(orderId).orElse(null);
        if (payment == null) {
            log.warn("Payment not found for PayPal orderId={}", orderId);
            return;
        }

        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
            return;
        }

        if ("COMPLETED".equalsIgnoreCase(captureStatus)) {
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            payment.setPaymentTime(LocalDateTime.now());
            payment.setPaypalCaptureId(captureId);
            payment.setTransactionId(captureId);
            syncBookingStatus(payment.getBooking(), BookingStatus.COMPLETED, PaymentStatus.COMPLETED);
            paymentRepository.save(payment);
            ticketIssuanceService.issueTicketAfterPaymentCompleted(payment);
        }
    }

    @SuppressWarnings("unchecked")
    private void onCaptureDenied(Map<String, Object> body) {
        Map<String, Object> resource = (Map<String, Object>) body.get("resource");
        if (resource == null) {
            return;
        }

        String orderId = extractOrderId(resource);
        log.info("PayPal capture denied. orderId={}", orderId);

        if (orderId == null || orderId.isBlank()) {
            return;
        }

        Payment payment = paymentRepository.findByPaypalOrderId(orderId).orElse(null);
        if (payment == null || payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
            return;
        }

        payment.setPaymentStatus(PaymentStatus.FAILED);
        syncBookingStatus(payment.getBooking(), BookingStatus.FAILED, PaymentStatus.FAILED);
        paymentRepository.save(payment);
    }

    private void syncBookingStatus(Booking booking, BookingStatus bookingStatus, PaymentStatus paymentStatus) {
        if (booking == null) {
            return;
        }
        booking.setStatus(bookingStatus);
        booking.setPaymentStatus(paymentStatus);
        bookingRepository.save(booking);
    }

    @SuppressWarnings("unchecked")
    private String extractOrderId(Map<String, Object> resource) {
        try {
            Map<String, Object> supplementary = (Map<String, Object>) resource.get("supplementary_data");
            Map<String, Object> related = (Map<String, Object>) supplementary.get("related_ids");
            return str(related.get("order_id"));
        } catch (Exception e) {
            log.debug("Unable to extract PayPal order id from webhook resource", e);
            return null;
        }
    }

    private String headerAny(Map<String, String> headers, String... keys) {
        if (headers == null || headers.isEmpty()) {
            return null;
        }

        for (String key : keys) {
            String value = headers.get(key);
            if (value != null && !value.isBlank()) {
                return value;
            }

            value = headers.get(key.toLowerCase());
            if (value != null && !value.isBlank()) {
                return value;
            }
        }

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }
            for (String key : keys) {
                if (entry.getKey().equalsIgnoreCase(key)) {
                    String value = entry.getValue();
                    if (value != null && !value.isBlank()) {
                        return value;
                    }
                }
            }
        }
        return null;
    }

    private String str(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
