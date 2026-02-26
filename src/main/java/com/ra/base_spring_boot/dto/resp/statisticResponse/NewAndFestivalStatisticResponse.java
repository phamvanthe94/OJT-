package com.ra.base_spring_boot.dto.resp.statisticResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewAndFestivalStatisticResponse {
    private Long totalNews;
    private List<NewAndFestivalResponse> news;

    private Long totalFestivals;
    private List<NewAndFestivalResponse> festivals;
}
