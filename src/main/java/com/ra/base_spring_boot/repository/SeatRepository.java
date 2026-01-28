package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.entity.theater.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat,Long> {
}
