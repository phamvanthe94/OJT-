package com.ra.base_spring_boot.dto.statistic.resp;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RevenueStatisticResponse {

    private Long groupId;         // id của phim, thể loại, rạp, phòng chiếu
    private String groupName;     // tên phim, tên thể loại, tên rạp, tên phòng chiếu
    private Double todayRevenue;
    private Double weekRevenue;
    private Double monthRevenue;
    private Double yearRevenue;
    private Double totalRevenue;
}
