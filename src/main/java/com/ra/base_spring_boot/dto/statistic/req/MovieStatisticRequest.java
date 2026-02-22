package com.ra.base_spring_boot.dto.statistic.req;

import lombok.*;

@Data
@Builder
@NoArgsConstructor

public class MovieStatisticRequest {

    // Hiện tại dashboard chưa cần filter
    // Sau này có thể mở rộng:
    // private Long cinemaId;
    // private MovieType type;

}
