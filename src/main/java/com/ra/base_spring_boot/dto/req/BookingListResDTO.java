package com.ra.base_spring_boot.dto.req;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingListResDTO {
    private Long id;
    private String username;
    private Long showTimeId;
    private Integer totalSeat;
    private Double totalPriceMovie;
    private String paymentStatus; // PaymentStatus.name()
    private LocalDateTime createdAt;
}
