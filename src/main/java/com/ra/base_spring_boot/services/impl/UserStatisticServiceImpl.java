package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.resp.UserStatisticResponse;
import com.ra.base_spring_boot.model.entity.user.User;
import com.ra.base_spring_boot.repository.IUserStatisticRepository;
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
        List<User> activeUsers = userStatisticRepository.findByStatus(true);
        List<User> inactiveUsers = userStatisticRepository.findByStatus(false);

        return UserStatisticResponse.builder()
                .total(buildGroup(allUsers))
                .active(buildGroup(activeUsers))
                .inactive(buildGroup(inactiveUsers))
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
                .fullName(user.getFullName())
                .username(user.getUsername())
                .status(user.getStatus())
                .build();
    }
}
