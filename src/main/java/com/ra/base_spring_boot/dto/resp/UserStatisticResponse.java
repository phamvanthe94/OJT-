package com.ra.base_spring_boot.dto.resp;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class UserStatisticResponse {
    private Group total;
    private Group active;
    private Group inactive;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Group {
        private Long count;
        private List<UserItem> users;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserItem {
        private Long id;
        private String fullName;
        private String username;
        private Boolean status;
    }
}
