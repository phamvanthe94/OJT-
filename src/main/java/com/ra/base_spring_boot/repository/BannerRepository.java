package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.entity.content.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    @Query("""
    SELECT b FROM Banner b
    WHERE (
        :search IS NULL 
        OR :search = ''
        OR b.url LIKE %:search%
        OR b.position LIKE %:search%
    )
""")
    Page<Banner> findByPosition(
            @Param("search") String search,
            Pageable pageable
    );
}
