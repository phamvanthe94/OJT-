package com.ra.base_spring_boot.services.homeService;

import com.ra.base_spring_boot.dto.resp.homeresp.TicketPriceResponse;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.constants.SeatType;

import java.util.List;

public interface ITicketHomeService {

    List<TicketPriceResponse> getTicketPrices(
            SeatType seatType,
            MovieType movieType,
            Boolean dayType
    );

}
