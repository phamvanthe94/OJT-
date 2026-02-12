package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.entity.booking.PaymentProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPaymentProviderRepository extends JpaRepository<PaymentProvider, Long> {

    boolean existsByProviderNameIgnoreCase(String providerName);

    boolean existsByProviderCodeIgnoreCase(String providerCode);

}
