package com.ra.base_spring_boot.services.Theater;

import com.ra.base_spring_boot.dto.req.TheaterReq.TheaterReq;
import com.ra.base_spring_boot.dto.resp.TheaterResp.TheaterResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface TheaterService {
    Page<TheaterResp> getTheaters(Pageable pageable);
    Page<TheaterResp> searchTheaterByName(String name, Pageable pageable);
    TheaterResp getTheaterById(Long theaterId);
    TheaterResp createTheater(TheaterReq theaterReq);
    TheaterResp updateTheater(Long theaterId, TheaterReq theaterReq);
    void deleteTheater(Long theaterId);
}
