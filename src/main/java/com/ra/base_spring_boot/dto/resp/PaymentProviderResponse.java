package com.ra.base_spring_boot.dto.resp;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentProviderResponse {
    private Long id;
    private String providerName;
    private String providerCode;
    private Boolean status;
    private String description;
}
