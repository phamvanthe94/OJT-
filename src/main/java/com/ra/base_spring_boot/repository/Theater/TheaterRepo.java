package com.ra.base_spring_boot.repository.Theater;

import com.ra.base_spring_boot.model.entity.theater.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepo extends JpaRepository<Theater, Long> {
    Page<Theater> findAllByNameContainingIgnoreCase(Pageable pageable, String name);
}
