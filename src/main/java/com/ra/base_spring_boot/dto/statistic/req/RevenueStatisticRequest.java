package com.ra.base_spring_boot.dto.statistic.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueStatisticRequest {

    private Long movieId;    // Lọc theo phim
    private Long genreId;    // Lọc theo thể loại phim
    private Long theaterId;  // Lọc theo rạp
    private Long screenId;   // Lọc theo phòng chiếu
}
