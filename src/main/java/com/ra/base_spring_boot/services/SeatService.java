package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.req.SeatReq;
import com.ra.base_spring_boot.dto.resp.SeatResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SeatService {
    Page<SeatResp> findAll(Pageable pageable);

    SeatResp findById(Long id);

    SeatResp createSeat(SeatReq seatReq);

    SeatResp updateSeat(Long id, SeatReq seatReq);

    void deleteSeat(Long id);
}
