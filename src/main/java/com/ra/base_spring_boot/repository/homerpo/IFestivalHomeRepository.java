package com.ra.base_spring_boot.repository.homerpo;

import com.ra.base_spring_boot.model.entity.content.Festival;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IFestivalHomeRepository extends JpaRepository<Festival, Long> {
    @Query("""
            SElECT f FROM Festival f
            ORDER BY f.startTime DESC
            """)
    Page<Festival> findAllFestivals(Pageable pageable);
}
