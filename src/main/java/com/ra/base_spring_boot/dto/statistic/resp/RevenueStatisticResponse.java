package com.ra.base_spring_boot.dto.statistic.resp;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RevenueStatisticResponse {

    private Long groupId;
    private String groupName;
    private Double todayRevenue;
    private Double weekRevenue;
    private Double monthRevenue;
    private Double yearRevenue;
    private Double totalRevenue;

    public RevenueStatisticResponse(Long groupId, String groupName,
                                    Long todayRevenue, Long weekRevenue, Long monthRevenue, Long yearRevenue,
                                    Double totalRevenue) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.todayRevenue = todayRevenue == null ? 0.0 : todayRevenue.doubleValue();
        this.weekRevenue = weekRevenue == null ? 0.0 : weekRevenue.doubleValue();
        this.monthRevenue = monthRevenue == null ? 0.0 : monthRevenue.doubleValue();
        this.yearRevenue = yearRevenue == null ? 0.0 : yearRevenue.doubleValue();
        this.totalRevenue = totalRevenue == null ? 0.0 : totalRevenue;
    }
}
