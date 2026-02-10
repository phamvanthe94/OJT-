package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.resp.UserStatisticResponse;
import com.ra.base_spring_boot.model.constants.UserStatus;
import com.ra.base_spring_boot.model.entity.user.User;
import com.ra.base_spring_boot.repository.statisticRp.IUserStatisticRepository;
import com.ra.base_spring_boot.services.IUserStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStatisticServiceImpl implements IUserStatisticService {

    private final IUserStatisticRepository userStatisticRepository;

    @Override
    public UserStatisticResponse getUserStatistic() {
        List<User> allUsers = userStatisticRepository.findAll();
        List<User> activeUsers = userStatisticRepository.findByStatus(UserStatus.ACTIVE);
        List<User> blockedUsers = userStatisticRepository.findByStatus(UserStatus.BLOCKED);

        return UserStatisticResponse.builder()
                .total(buildGroup(allUsers))
                .active(buildGroup(activeUsers))
                .blocked(buildGroup(blockedUsers))
                .build();
    }

    private UserStatisticResponse.Group buildGroup(List<User> users) {
        return UserStatisticResponse.Group.builder()
                .count((long) users.size())
                .users(users.stream().map(this::toUserItem).toList())
                .build();
    }

    private UserStatisticResponse.UserItem toUserItem(User user) {
        return UserStatisticResponse.UserItem.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .status(user.getStatus())
                .build();
    }
}
