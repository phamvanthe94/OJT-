package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.entity.content.Festival;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FestivalRepository extends JpaRepository<Festival, Long> {
    @Query("""
        SELECT f FROM Festival f
        WHERE (:search IS NULL OR f.title LIKE %:search%)
    """)
    Page<Festival> findByTitle(String search, Pageable pageable);
}
