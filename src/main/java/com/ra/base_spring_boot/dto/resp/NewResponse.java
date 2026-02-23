package com.ra.base_spring_boot.dto.resp;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NewResponse {
    private Long id;
    private String title;
    private String content;
    private String festivalTitle;
    private LocalDateTime createdAt;
}
