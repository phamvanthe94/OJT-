package com.ra.base_spring_boot.dto.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChoosePaymentSelectionDTO {
    private Long providerId;
    private String paymentMethod;
}
