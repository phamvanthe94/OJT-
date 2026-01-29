package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.req.ScreenReq;
import com.ra.base_spring_boot.dto.resp.ScreenResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScreenService {
    Page<ScreenResp> findAll(Pageable pageable);
    Page<ScreenResp> findByName(Pageable pageable, String name);
    ScreenResp findById(Long id);
    ScreenResp createScreen (ScreenReq screenReq);
    ScreenResp updateScreen (Long id, ScreenReq screenReq);
    void deleteScreen (Long id);
}
