package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.dto.resp.ScreenResp;
import com.ra.base_spring_boot.model.entity.theater.Screen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {
    Page<Screen> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
