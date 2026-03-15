package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.FormCreatePaymentProvider;
import com.ra.base_spring_boot.dto.resp.PaymentProviderResponse;
import com.ra.base_spring_boot.exception.HttpConflict;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.entity.booking.PaymentProvider;
import com.ra.base_spring_boot.repository.payment.IPaymentProviderRepository;
import com.ra.base_spring_boot.services.IPaymentProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentProviderServiceImpl implements IPaymentProviderService {

    private final IPaymentProviderRepository paymentProviderRepository;

    @Override
    public List<PaymentProviderResponse> findAll() {
        return paymentProviderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PaymentProviderResponse create(FormCreatePaymentProvider formCreatePaymentProvider) {
        String name = formCreatePaymentProvider.getProviderName().trim();
        String code = formCreatePaymentProvider.getProviderCode().trim().toUpperCase();

        if (paymentProviderRepository.existsByProviderNameIgnoreCase(name)) {
            throw new HttpConflict("Payment provider name already exists");
        }
        if (paymentProviderRepository.existsByProviderCodeIgnoreCase(code)) {
            throw new HttpConflict("Payment provider code already exists");
        }

        PaymentProvider savedProvider = paymentProviderRepository.save(PaymentProvider.builder()
                .providerName(name)
                .providerCode(code)
                .description(formCreatePaymentProvider.getDescription())
                .status(true)
                .build());

        return toResponse(savedProvider);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        PaymentProvider paymentProvider = paymentProviderRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Payment provider not found"));
        paymentProviderRepository.delete(paymentProvider);
    }

    private PaymentProviderResponse toResponse(PaymentProvider paymentProvider) {
        return PaymentProviderResponse.builder()
                .id(paymentProvider.getId())
                .providerName(paymentProvider.getProviderName())
                .providerCode(paymentProvider.getProviderCode())
                .description(paymentProvider.getDescription())
                .status(paymentProvider.getStatus())
                .build();
    }
}
