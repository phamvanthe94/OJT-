package com.ra.base_spring_boot.dto.resp;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketQrData {
    private Long bookingId;
    private String fullName;

    private String title;
    private String screenName;

    private LocalDateTime startTime;
    private String seatNumber;
}

