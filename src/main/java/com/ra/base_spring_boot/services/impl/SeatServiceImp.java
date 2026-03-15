package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.SeatReq;
import com.ra.base_spring_boot.dto.resp.SeatResp;
import com.ra.base_spring_boot.dto.resp.SeatSelectResp;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.constants.SeatType;
import com.ra.base_spring_boot.model.entity.theater.Seat;
import com.ra.base_spring_boot.model.entity.theater.ShowTime;
import com.ra.base_spring_boot.repository.booking.IShowTimeRepository;
import com.ra.base_spring_boot.repository.booking.ISeatRepository;
import com.ra.base_spring_boot.services.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImp implements SeatService {
    private final ISeatRepository seatRepository;
    private final IShowTimeRepository showTimeRepository;


    @Override
    public Page<SeatResp> findAll(Pageable pageable) {
        Page<Seat> seats = seatRepository.findAll(pageable);

        return seats.map(seat -> SeatResp.builder()
                .seatNumber(seat.getSeatNumber())
                .isVariable(seat.getIsVariable())
                .type(seat.getType())
                .createdAt(seat.getCreatedAt())
                .updatedAt(seat.getUpdatedAt())
                .build());
    }

    @Override
    public SeatResp findById(Long id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Seat not found with id = " + id));

        return SeatResp.builder()
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
                .seatNumber(seatReq.getSeatNumber())
                .isVariable(seatReq.getIsVariable())
                .type(seatReq.getType())
                .build();

        seatRepository.save(seat);

        return SeatResp.builder()
                .seatNumber(seat.getSeatNumber())
                .isVariable(seat.getIsVariable())
                .type(seat.getType())
                .createdAt(seat.getCreatedAt())
                .updatedAt(seat.getUpdatedAt())
                .build();
    }

    @Override
    public SeatResp updateSeat(Long id, SeatReq seatReq) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Seat not found with id = " + id));

        seat.setSeatNumber(seatReq.getSeatNumber());
        seat.setIsVariable(seatReq.getIsVariable());
        seat.setType(seatReq.getType());

        seatRepository.save(seat);

        return SeatResp.builder()
                .seatNumber(seat.getSeatNumber())
                .isVariable(seat.getIsVariable())
                .type(seat.getType())
                .createdAt(seat.getCreatedAt())
                .updatedAt(seat.getUpdatedAt())
                .build();
    }

    @Override
    public void deleteSeat(Long id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Seat not found with id = " + id));

        seatRepository.delete(seat);
    }

    @Override
    public List<SeatSelectResp> getSeatsByShowTime(Long showTimeId) {

        ShowTime showTime = showTimeRepository.findById(showTimeId)
                .orElseThrow(() -> new HttpNotFound("Showtime not found"));

        Long screenId = showTime.getScreen().getId();

        return seatRepository.findSeatsWithBookingStatus(screenId, showTimeId)
                .stream()
                .map(obj -> SeatSelectResp.builder()
                        .seatId(((Number) obj[0]).longValue())
                        .seatNumber((String) obj[1])
                        .type(SeatType.valueOf(obj[2].toString()))
                        .booked(toBoolean(obj[3]))
                        .build())
                .toList();
    }

    private boolean toBoolean(Object value) {
        if (value instanceof Boolean booleanValue) {
            return booleanValue;
        }
        if (value instanceof Number numberValue) {
            return numberValue.longValue() != 0L;
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }

}
