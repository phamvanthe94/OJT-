package com.ra.base_spring_boot.dto.req;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDetailResDTO {

    private Long id;

    private String username;
    private String email;
    private String phone;

    private Long showTimeId;

    private Integer totalSeat;
    private Double totalPriceMovie;

    private String paymentStatus;
    private Double amount;
    private String paymentMethod;
    private LocalDateTime paymentTime;
    private String transactionId;

    private List<BookingSeatRequest> seats;

    private LocalDateTime createdAt;
}
