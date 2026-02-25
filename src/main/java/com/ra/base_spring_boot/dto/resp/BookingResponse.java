package com.ra.base_spring_boot.dto.resp;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookingResponse {

    private Long bookingId;

    private String bookingCode;

    private Long showTimeId;

    private String screenName;

    private Integer totalSeat;

    private Double totalAmount;

    private String status;

    private String paymentStatus;

    private String qrCode;

    private LocalDateTime createdAt;

    private List<Long> seatIds;
}
