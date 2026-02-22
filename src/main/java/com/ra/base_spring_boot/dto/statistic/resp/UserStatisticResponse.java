package com.ra.base_spring_boot.dto.statistic.resp;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserStatisticResponse {
    private Long totalUsers;
    private Long totalActiveUsers;
    private Long totalBlockedUsers;
}
