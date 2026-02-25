package com.ra.base_spring_boot.services.statistic;

import com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByMovieStatisticResponse;
import com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByScreenStatisticResponse;

public interface ITicketStatisticService {
    TicketByMovieStatisticResponse statisticByMovie();
    TicketByScreenStatisticResponse statisticByScreen();
}
