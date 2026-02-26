package com.ra.base_spring_boot.services.statistic;

import com.ra.base_spring_boot.dto.statistic.req.UserStatisticRequest;
import com.ra.base_spring_boot.dto.statistic.resp.UserStatisticResponse;

public interface IUserStatisticService {
    UserStatisticResponse getDashboardStatistic(UserStatisticRequest request);
}
