package com.ra.base_spring_boot.repository.payment;

import com.ra.base_spring_boot.model.entity.booking.PaymentProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPaymentProviderRepository extends JpaRepository<PaymentProvider, Long> {

    boolean existsByProviderNameIgnoreCase(String providerName);

    boolean existsByProviderCodeIgnoreCase(String providerCode);

    Optional<PaymentProvider> findByProviderCode(String providerCode);

    Optional<PaymentProvider> findByProviderCodeIgnoreCase(String providerCode);
}
