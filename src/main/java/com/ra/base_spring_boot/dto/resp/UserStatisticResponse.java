package com.ra.base_spring_boot.dto.resp;

import com.ra.base_spring_boot.model.constants.UserStatus;
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
    private Group blocked;

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
        private String firstName;
        private String lastName;
        private String email;
        private UserStatus status;
    }
}
