package com.ra.base_spring_boot.dto.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingSeatRequest {
    private Long seatId;
    private String seatName;
    private Integer quantity;
    private Long ticketPriceId;
}
