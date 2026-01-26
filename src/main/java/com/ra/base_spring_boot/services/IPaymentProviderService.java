package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.req.FormCreatePaymentProvider;
import com.ra.base_spring_boot.dto.resp.PaymentProviderResponse;

import java.util.List;

public interface IPaymentProviderService {
    List<PaymentProviderResponse> findAll();

    PaymentProviderResponse create(FormCreatePaymentProvider formCreatePaymentProvider);

    void delete(Long id);
}
