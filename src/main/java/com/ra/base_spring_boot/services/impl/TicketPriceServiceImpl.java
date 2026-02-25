package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.TicketPriceRequest;
import com.ra.base_spring_boot.dto.resp.TicketPriceResponse;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.constants.SeatType;
import com.ra.base_spring_boot.model.entity.booking.TicketPrice;
import com.ra.base_spring_boot.repository.booking.ITicketPriceRepository;
import com.ra.base_spring_boot.services.ITicketPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketPriceServiceImpl implements ITicketPriceService {

    private final ITicketPriceRepository ticketPriceRepository;

    @Override
    public Page<TicketPriceResponse> getAllAndSearchTicketPrice(
            Boolean dayType,
            String seatType,
            String movieType,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        SeatType seatTypeEnum = seatType != null && !seatType.isBlank()
                ? SeatType.valueOf(seatType)
                : null;

        MovieType movieTypeEnum = movieType != null && !movieType.isBlank()
                ? MovieType.from(movieType)
                : null;

        return ticketPriceRepository
                .search(dayType, seatTypeEnum, movieTypeEnum, pageable)
                .map(this::toResponse);
    }

    @Override
    public TicketPriceResponse createTicketPrice(TicketPriceRequest request) {
        TicketPrice ticketPrice = convertToEntity(request);
        return toResponse(ticketPriceRepository.save(ticketPrice));
    }

    @Override
    public TicketPriceResponse updateTicketPrice(Long id, TicketPriceRequest request) {
        TicketPrice old = ticketPriceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket Price not found"));

        TicketPrice updated = convertToEntity(request);
        updated.setId(old.getId());

        return toResponse(ticketPriceRepository.save(updated));
    }

    @Override
    public void deleteTicketPrice(Long id) {
        TicketPrice ticketPrice = ticketPriceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket Price not found"));
        ticketPriceRepository.delete(ticketPrice);
    }

    /* ===== helper ===== */

    private TicketPrice convertToEntity(TicketPriceRequest req) {
        return TicketPrice.builder()
                .typeSeat(SeatType.valueOf(req.getTypeSeat()))
                .typeMovie(MovieType.from(req.getTypeMovie()))
                .price(req.getPrice())
                .dayType(req.getDayType())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .build();
    }

    private TicketPriceResponse toResponse(TicketPrice entity) {
        return TicketPriceResponse.builder()
                .typeSeat(entity.getTypeSeat().name())
                .typeMovie(entity.getTypeMovie().toValue())
                .price(entity.getPrice())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .build();
    }
}
