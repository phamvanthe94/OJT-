package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.FormCreatePaymentProvider;
import com.ra.base_spring_boot.dto.resp.PaymentProviderResponse;
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
                .map(paymentProvider -> PaymentProviderResponse.builder()
                        .id(paymentProvider.getId())
                        .providerName(paymentProvider.getProviderName())
                        .providerCode(paymentProvider.getProviderCode())
                        .description(paymentProvider.getDescription())
                        .status(paymentProvider.getStatus())
                        .build())
                .toList();
    }

    @Override
    public PaymentProviderResponse create(FormCreatePaymentProvider formCreatePaymentProvider) {

        String name = formCreatePaymentProvider.getProviderName().trim();
        String code = formCreatePaymentProvider.getProviderCode().trim().toUpperCase();

        if (paymentProviderRepository.existsByProviderCodeIgnoreCase(name)) {
            throw new RuntimeException("Tên nhà cung cấp đã tồn tại");
        }
        if (paymentProviderRepository.existsByProviderCodeIgnoreCase(code)) {
            throw new RuntimeException("Mã nhà cung cấp đã tồn tại");
        }

        PaymentProvider paymentProvider = PaymentProvider.builder()
                .providerName(name)
                .providerCode(code)
                .description(formCreatePaymentProvider.getDescription())
                .status(true)
                .build();

        PaymentProvider savedProvider = paymentProviderRepository.save(paymentProvider);

        return PaymentProviderResponse.builder()
                .id(savedProvider.getId())
                .providerName(savedProvider.getProviderName())
                .providerCode(savedProvider.getProviderCode())
                .description(savedProvider.getDescription())
                .status(savedProvider.getStatus())
                .build();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        PaymentProvider paymentProvider = paymentProviderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nhà cung cấp không tồn tại"));
        paymentProviderRepository.delete(paymentProvider);

    }
}
