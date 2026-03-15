package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.ScreenReq;
import com.ra.base_spring_boot.dto.resp.ScreenResp;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.entity.theater.Screen;
import com.ra.base_spring_boot.repository.ScreenRepository;
import com.ra.base_spring_boot.services.ScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScreenServiceImp implements ScreenService {
    private final ScreenRepository screenRepository;
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
        if (screens.isEmpty()) {
            throw new HttpNotFound("No screens found with name: " + name);
        }
        return screens.map(screen -> ScreenResp.builder()
                .name(screen.getName())
                .seatCapacity(screen.getSeatCapacity())
                .createdAt(screen.getCreatedAt())
                .updatedAt(screen.getUpdatedAt())
                .build());
    }

    @Override
    public ScreenResp findById(Long id) {
        Screen screen = screenRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Screen not found with id = " + id));
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
                .orElseThrow(() -> new HttpNotFound("Screen not found with id = " + id));
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
                .orElseThrow(() -> new HttpNotFound("Screen not found with id = " + id));
        screenRepository.deleteById(id);

    }
}
