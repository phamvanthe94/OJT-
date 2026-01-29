package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.entity.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // ✅ KHÔNG @Query, KHÔNG search -> tránh đụng field User không tồn tại
}