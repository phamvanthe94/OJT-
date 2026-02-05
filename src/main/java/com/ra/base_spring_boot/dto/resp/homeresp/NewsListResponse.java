package com.ra.base_spring_boot.dto.resp.homeresp;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsListResponse {
    private Long id;
    private String title;
    private String excerpt;
    private Long festivalId;
    private LocalDateTime createdAt;
}
