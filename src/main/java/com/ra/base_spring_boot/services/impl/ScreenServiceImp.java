package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.ScreenReq;
import com.ra.base_spring_boot.dto.resp.ScreenResp;
import com.ra.base_spring_boot.model.entity.theater.Screen;
import com.ra.base_spring_boot.repository.ScreenRepository;
import com.ra.base_spring_boot.services.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ScreenServiceImp implements ScreenService {
    @Autowired
    ScreenRepository screenRepository;
    @Override
    public Page<ScreenResp> findAll(Pageable pageable) {
        Page<Screen> screens= screenRepository.findAll(pageable);
        return screens.map(screen -> ScreenResp.builder()
                .name(screen.getName())
                .seatCapacity(screen.getSeatCapacity())
                .createdAt(screen.getCreatedAt())
                .updatedAt(screen.getUpdatedAt())
                .build());
    }

    @Override
    public Page<ScreenResp> findByName(Pageable pageable, String name) {
        Page<Screen> screens = screenRepository.findAllByNameContainingIgnoreCase(name, pageable);

        return screens.map(screen -> ScreenResp.builder()
                .name(screen.getName())
                .seatCapacity(screen.getSeatCapacity())
                .createdAt(screen.getCreatedAt())
                .updatedAt(screen.getUpdatedAt())
                .build());
    }

    @Override
    public ScreenResp findById(Long id) {
        Screen screen = screenRepository.findById(id).orElseThrow(()->new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Screen not found with id = " + id));

        return ScreenResp.builder()
                .name(screen.getName())
                .seatCapacity(screen.getSeatCapacity())
                .createdAt(screen.getCreatedAt())
                .updatedAt(screen.getUpdatedAt())
                .build();
    }

    @Override
    public ScreenResp createScreen(ScreenReq screenReq) {
        Screen screen= new Screen();
        screen.setName(screenReq.getName());
        screen.setSeatCapacity(screenReq.getSeatCapacity());
        screenRepository.save(screen);
        return ScreenResp.builder()
                .name(screen.getName())
                .seatCapacity(screen.getSeatCapacity())
                .createdAt(screen.getCreatedAt())
                .updatedAt(screen.getUpdatedAt())
                .build();
    }

    @Override
    public ScreenResp updateScreen(Long id, ScreenReq screenReq) {
        Screen screen = screenRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Screen not found with id = " + id
                ));
        screen.setId(id);
        screen.setName(screenReq.getName());
        screen.setSeatCapacity(screenReq.getSeatCapacity());
        screenRepository.save(screen);
        return ScreenResp.builder()
                .name(screen.getName())
                .seatCapacity(screen.getSeatCapacity())
                .createdAt(screen.getCreatedAt())
                .updatedAt(screen.getUpdatedAt())
                .build();
    }

    @Override
    public void deleteScreen(Long id) {
        Screen screen = screenRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Screen not found with id = " + id
                ));
        screenRepository.deleteById(id);

    }
}
