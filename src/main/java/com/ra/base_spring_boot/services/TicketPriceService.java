package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.TicketPriceDTO;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.constants.SeatType;
import com.ra.base_spring_boot.model.entity.booking.TicketPrice;
import com.ra.base_spring_boot.repository.TicketPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class TicketPriceService {
    @Autowired
    private TicketPriceRepository ticketPriceRepository;

    // Java
    public ResponseEntity<ResponseWrapper<Page<TicketPrice>>> getAllAndSearchTicketPrice(Boolean dayType,
                                                                                         String seatType, String movieType, Pageable pageable) {
        SeatType seatTypeEnum = null;
        if (seatType != null && !seatType.isBlank()) {
            try {
                seatTypeEnum = SeatType.valueOf(seatType.trim());
            } catch (IllegalArgumentException ex) {
                ResponseWrapper<String> resp = ResponseWrapper.<String>builder()
                        .data("Invalid Seat Type: " + seatType)
                        .code(400)
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        MovieType movieTypeEnum = null;
        if (movieType != null && !movieType.isBlank()) {
            try {
                movieTypeEnum = MovieType.valueOf(movieType.trim());
            } catch (IllegalArgumentException ex) {
                ResponseWrapper<String> resp = ResponseWrapper.<String>builder()
                        .data("Invalid Movie Type: " + movieType)
                        .code(400)
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        Page<TicketPrice> ticketPricePage = ticketPriceRepository.search(dayType, seatTypeEnum, movieTypeEnum, pageable);
        ResponseWrapper<Page<TicketPrice>> responseWrapper = ResponseWrapper.<Page<TicketPrice>>builder()
                .data(ticketPricePage)
                .code(200)
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(responseWrapper);
    }

public ResponseEntity<ResponseWrapper<?>> createTicketPrice(TicketPriceDTO ticketPriceDTO, BindingResult bindingResult){
    if(ticketPriceDTO == null){
        bindingResult.rejectValue("price", "400", "Ticket Price data cannot be null");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }else {
        TicketPrice ticketPrice = convertTicketPriceDTOToTicketPrice(ticketPriceDTO);
        TicketPrice newTicketPrice = ticketPriceRepository.save(ticketPrice);
        ResponseWrapper<TicketPrice> responseWrapper = ResponseWrapper
                .<TicketPrice>builder()
                .data(newTicketPrice)
                .code(201)
                .status(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(responseWrapper, HttpStatus.CREATED);
    }
}
public ResponseEntity<ResponseWrapper<?>> updateTicketPrice(Long id, TicketPriceDTO ticketPriceDTO){
    TicketPrice oldTicketPrice = ticketPriceRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket Price not found with id: " + id));
    if(ticketPriceDTO != null){
        TicketPrice ticketPrice = convertTicketPriceDTOToTicketPrice(ticketPriceDTO);
        ticketPrice.setId(oldTicketPrice.getId());
        TicketPrice updatedTicketPrice = ticketPriceRepository.save(ticketPrice);
        ResponseWrapper<TicketPrice> responseWrapper = ResponseWrapper
                .<TicketPrice>builder()
                .data(updatedTicketPrice)
                .code(200)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
public ResponseEntity<ResponseWrapper<String>> deleteTicketPrice(Long id){
    TicketPrice ticketPrice = ticketPriceRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket Price not found with id: " + id));
    ticketPriceRepository.delete(ticketPrice);
    ResponseWrapper<String> responseWrapper = ResponseWrapper
            .<String>builder()
            .data("Delete Ticket Price successfully")
            .code(200)
            .status(HttpStatus.OK)
            .build();
    return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
}
    public TicketPrice convertTicketPriceDTOToTicketPrice(TicketPriceDTO ticketPriceDTO) {

        SeatType seatType = null;
        if (ticketPriceDTO.getTypeSeat() != null && !ticketPriceDTO.getTypeSeat().isBlank()) {
            try {
                seatType = SeatType.valueOf(ticketPriceDTO.getTypeSeat().trim());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid Seat Type: " + ticketPriceDTO.getTypeSeat());
            }
        }

        MovieType movieType = null;
        if (ticketPriceDTO.getTypeMovie() != null && !ticketPriceDTO.getTypeMovie().isBlank()) {
            try {
                movieType = MovieType.from(ticketPriceDTO.getTypeMovie().trim());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid Movie Type: " + ticketPriceDTO.getTypeMovie());
            }
        }

        return TicketPrice.builder()
                .typeSeat(seatType)
                .typeMovie(movieType)
                .price(ticketPriceDTO.getPrice())
                .dayType(ticketPriceDTO.getDayType())
                .startTime(ticketPriceDTO.getStartTime())
                .endTime(ticketPriceDTO.getEndTime())
                .build();
    }


}
