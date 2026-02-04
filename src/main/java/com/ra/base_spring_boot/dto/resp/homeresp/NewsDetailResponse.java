package com.ra.base_spring_boot.dto.resp.homeresp;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsDetailResponse {
    private Long id;
    private String title;
    private String content;
    private Long festivalId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
