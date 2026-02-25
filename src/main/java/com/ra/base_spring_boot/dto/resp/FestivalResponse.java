package com.ra.base_spring_boot.dto.resp;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FestivalResponse {
    private Long id;
    private String title;
    private String image;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
