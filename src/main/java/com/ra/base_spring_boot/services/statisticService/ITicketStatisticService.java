package com.ra.base_spring_boot.services.statisticService;

import com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByMovieStatisticResponse;
import com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByScreenStatisticResponse;

public interface ITicketStatisticService {
    TicketByMovieStatisticResponse statisticByMovie();
    TicketByScreenStatisticResponse statisticByScreen();
}
