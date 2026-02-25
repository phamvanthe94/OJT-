package com.ra.base_spring_boot.services;


import com.ra.base_spring_boot.dto.req.TicketPriceRequest;
import com.ra.base_spring_boot.dto.resp.TicketPriceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITicketPriceService {

    Page<TicketPriceResponse> getAllAndSearchTicketPrice(
            Boolean dayType,
            String seatType,
            String movieType,
            int page,
            int size,
            String sortBy,
            String direction
    );

    TicketPriceResponse createTicketPrice(TicketPriceRequest request);

    TicketPriceResponse updateTicketPrice(Long id, TicketPriceRequest request);

    void deleteTicketPrice(Long id);
}


