package com.ra.base_spring_boot.services.statistic.impl;

import com.ra.base_spring_boot.dto.statistic.req.UserStatisticRequest;
import com.ra.base_spring_boot.dto.statistic.resp.UserStatisticResponse;
import com.ra.base_spring_boot.model.constants.UserStatus;
import com.ra.base_spring_boot.repository.statistic.IUserStatisticRepository;
import com.ra.base_spring_boot.services.statistic.IUserStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatisticServiceImpl implements IUserStatisticService {

    private final IUserStatisticRepository userStatisticRepository;

    @Override
    public UserStatisticResponse getDashboardStatistic(UserStatisticRequest request) {

        long totalUsers = userStatisticRepository.count();
        long activeUsers = userStatisticRepository.countByStatus(UserStatus.ACTIVE);
        long blockedUsers = userStatisticRepository.countByStatus(UserStatus.BLOCKED);

        return UserStatisticResponse.builder()
                .totalUsers(totalUsers)
                .totalActiveUsers(activeUsers)
                .totalBlockedUsers(blockedUsers)
                .build();
    }
}
