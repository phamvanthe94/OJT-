package com.ra.base_spring_boot.services.statisticService.impl;

import com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByMovieResponse;
import com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByMovieStatisticResponse;
import com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByScreenResponse;
import com.ra.base_spring_boot.dto.resp.statisticResponse.TicketByScreenStatisticResponse;
import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.repository.statisticRp.ITicketStatisticRepository;
import com.ra.base_spring_boot.services.statisticService.ITicketStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TicketStatisticServiceImpl implements ITicketStatisticService {
    private final ITicketStatisticRepository bookingSeatRepository;


    public TicketByMovieStatisticResponse statisticByMovie(){
        List<TicketByMovieResponse> ticketByMovieResponses = bookingSeatRepository.statisticTicketByMovie(PaymentStatus.COMPLETED);
        Long totalTickets = ticketByMovieResponses.stream()
                .mapToLong(TicketByMovieResponse::getTotalTicket)
                .sum();
        return new TicketByMovieStatisticResponse(
                ticketByMovieResponses,
                totalTickets
        );

    }
    public TicketByScreenStatisticResponse statisticByScreen(){
        List<TicketByScreenResponse> ticketByScreenResponses = bookingSeatRepository.statisticTicketByScreen(PaymentStatus.COMPLETED);
        Long totalTickets = ticketByScreenResponses.stream()
                .mapToLong(TicketByScreenResponse::getTotalTicket)
                .sum();
        return new TicketByScreenStatisticResponse(
                ticketByScreenResponses,
                totalTickets
        );
    }
}
