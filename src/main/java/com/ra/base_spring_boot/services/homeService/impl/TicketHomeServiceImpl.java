package com.ra.base_spring_boot.services.homeService.impl;

import com.ra.base_spring_boot.dto.resp.homeresp.TicketPriceResponse;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.constants.SeatType;
import com.ra.base_spring_boot.model.entity.booking.TicketPrice;
import com.ra.base_spring_boot.repository.homerpo.ITicketPriceHomeRepository;
import com.ra.base_spring_boot.services.homeService.ITicketHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketHomeServiceImpl implements ITicketHomeService {

    private final ITicketPriceHomeRepository ticketPriceHomeRepository;

    @Override
    public List<TicketPriceResponse> getTicketPrices(SeatType seatType, MovieType movieType, Boolean dayType) {

        List<TicketPrice> ticketPriceList = ticketPriceHomeRepository.findTicketPriceBy(
                seatType,
                movieType,
                dayType
        );

        return ticketPriceList.stream().
                map(ticketPrice -> TicketPriceResponse.builder()
                        .id(ticketPrice.getId())
                        .seatType(ticketPrice.getTypeSeat())
                        .movieType(ticketPrice.getTypeMovie())
                        .price(ticketPrice.getPrice())
                        .dayType(ticketPrice.getDayType())
                        .build())
                .toList();
    }
}
