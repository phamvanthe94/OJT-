package com.ra.base_spring_boot.dto.statistic.resp;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class RevenueStatisticResponse {

    private String label;      // tên phim / thể loại / ngày-tháng-năm
    private Double revenue;    // tổng doanh thu
}
