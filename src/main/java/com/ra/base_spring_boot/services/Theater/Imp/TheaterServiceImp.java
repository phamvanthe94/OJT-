package com.ra.base_spring_boot.services.Theater.Imp;

import com.ra.base_spring_boot.dto.req.TheaterReq.TheaterReq;
import com.ra.base_spring_boot.dto.resp.TheaterResp.TheaterResp;
import com.ra.base_spring_boot.model.entity.theater.Theater;
import com.ra.base_spring_boot.repository.Theater.TheaterRepo;
import com.ra.base_spring_boot.services.Theater.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TheaterServiceImp implements TheaterService {
    @Autowired
    TheaterRepo theaterRepo;
    @Override
    public Page<TheaterResp> getTheaters(Pageable pageable) {
        Page<Theater> theaters= theaterRepo.findAll(pageable);
        return theaters.map(theater -> TheaterResp.builder()
                .name(theater.getName())
                .phone(theater.getPhone())
                .location(theater.getLocation())
                .build());
    }

    @Override
    public Page<TheaterResp> searchTheaterByName(String name, Pageable pageable) {
        Page<Theater> theaters = theaterRepo
                .findAllByNameContainingIgnoreCase(pageable,name);

        return theaters.map(theater ->
                TheaterResp.builder()
                        .name(theater.getName())
                        .phone(theater.getPhone())
                        .location(theater.getLocation())
                        .build());
    }

    @Override
    public TheaterResp getTheaterById(Long theaterId) {
        Theater theater = theaterRepo.findById(theaterId)
                .orElseThrow(() -> new RuntimeException("Theater not found"));

        return TheaterResp.builder()
                .name(theater.getName())
                .phone(theater.getPhone())
                .location(theater.getLocation())
                .build();
    }

    @Override
    public TheaterResp createTheater(TheaterReq theaterReq) {
        Theater theater= Theater.builder()
                .name(theaterReq.getName())
                .phone(theaterReq.getPhone())
                .location(theaterReq.getLocation())
                .build();
        theaterRepo.save(theater);
        return TheaterResp.builder()
                .name(theaterReq.getName())
                .phone(theaterReq.getPhone())
                .location(theaterReq.getLocation())
                .build();
    }

    @Override
    public TheaterResp updateTheater(Long theaterId, TheaterReq theaterReq) {
        Theater theater = theaterRepo.findById(theaterId)
                .orElseThrow(() -> new RuntimeException("Theater not found"));

        theater.setName(theaterReq.getName());
        theater.setPhone(theaterReq.getPhone());
        theater.setLocation(theaterReq.getLocation());

        Theater saved = theaterRepo.save(theater);

        return TheaterResp.builder()
                .name(saved.getName())
                .phone(saved.getPhone())
                .location(saved.getLocation())
                .build();
    }

    @Override
    public void deleteTheater(Long theaterId) {
        theaterRepo.deleteById(theaterId);

    }
}
