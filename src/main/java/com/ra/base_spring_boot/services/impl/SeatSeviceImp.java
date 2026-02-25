package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.SeatReq;
import com.ra.base_spring_boot.dto.resp.SeatResp;
import com.ra.base_spring_boot.dto.resp.SeatSelectResp;
import com.ra.base_spring_boot.model.constants.SeatType;
import com.ra.base_spring_boot.model.entity.theater.Seat;
import com.ra.base_spring_boot.model.entity.theater.ShowTime;
import com.ra.base_spring_boot.repository.booking.IShowTimeRepository;
import com.ra.base_spring_boot.repository.booking.ISeatRepository;
import com.ra.base_spring_boot.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SeatSeviceImp implements SeatService {
    @Autowired
    private ISeatRepository ISeatRepository;
    @Autowired
    private IShowTimeRepository showTimeRepository;


    @Override
    public Page<SeatResp> findAll(Pageable pageable) {
        Page<Seat> seats = ISeatRepository.findAll(pageable);

        return seats.map(seat -> SeatResp.builder()
                .screen(seat.getScreen())
                .seatNumber(seat.getSeatNumber())
                .isVariable(seat.getIsVariable())
                .type(seat.getType())
                .createdAt(seat.getCreatedAt())
                .updatedAt(seat.getUpdatedAt())
                .build());
    }

    @Override
    public SeatResp findById(Long id) {
        Seat seat = ISeatRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Seat not found with id = " + id
                ));

        return SeatResp.builder()
                .screen(seat.getScreen())
                .seatNumber(seat.getSeatNumber())
                .isVariable(seat.getIsVariable())
                .type(seat.getType())
                .createdAt(seat.getCreatedAt())
                .updatedAt(seat.getUpdatedAt())
                .build();
    }

    @Override
    public SeatResp createSeat(SeatReq seatReq) {
        Seat seat = Seat.builder()
                .screen(seatReq.getScreen())
                .seatNumber(seatReq.getSeatNumber())
                .isVariable(seatReq.getIsVariable())
                .type(seatReq.getType())
                .build();

        ISeatRepository.save(seat);

        return SeatResp.builder()
                .screen(seat.getScreen())
                .seatNumber(seat.getSeatNumber())
                .isVariable(seat.getIsVariable())
                .type(seat.getType())
                .createdAt(seat.getCreatedAt())
                .updatedAt(seat.getUpdatedAt())
                .build();
    }

    @Override
    public SeatResp updateSeat(Long id, SeatReq seatReq) {
        Seat seat = ISeatRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Seat not found with id = " + id
                ));

        seat.setScreen(seatReq.getScreen());
        seat.setSeatNumber(seatReq.getSeatNumber());
        seat.setIsVariable(seatReq.getIsVariable());
        seat.setType(seatReq.getType());

        ISeatRepository.save(seat);

        return SeatResp.builder()
                .screen(seat.getScreen())
                .seatNumber(seat.getSeatNumber())
                .isVariable(seat.getIsVariable())
                .type(seat.getType())
                .createdAt(seat.getCreatedAt())
                .updatedAt(seat.getUpdatedAt())
                .build();
    }

    @Override
    public void deleteSeat(Long id) {
        Seat seat = ISeatRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Seat not found with id = " + id
                ));

        ISeatRepository.delete(seat);
    }

    @Override
    public List<SeatSelectResp> getSeatsByShowTime(Long showTimeId) {

        ShowTime showTime = showTimeRepository.findById(showTimeId)
                .orElseThrow(() -> new RuntimeException("ShowTime not found"));

        Long screenId = showTime.getScreen().getId();

        return ISeatRepository.findSeatsWithBookingStatus(screenId, showTimeId)
                .stream()
                .map(obj -> SeatSelectResp.builder()
                        .seatId(((Number) obj[0]).longValue())
                        .seatNumber((String) obj[1])
                        .type(SeatType.valueOf(obj[2].toString()))
                        .booked((Boolean) obj[3])
                        .build())
                .toList();
    }


}
