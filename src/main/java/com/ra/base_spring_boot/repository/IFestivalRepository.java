package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.dto.resp.statisticResponse.NewAndFestivalResponse;
import com.ra.base_spring_boot.model.entity.content.Festival;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IFestivalRepository extends JpaRepository<Festival, Long> {
    @Query("""
        SELECT f FROM Festival f
        WHERE (:search IS NULL OR f.title LIKE %:search%)
    """)
    Page<Festival> findByTitle(String search, Pageable pageable);
    @Query("""
    SELECT new com.ra.base_spring_boot.dto.resp.statisticResponse.NewAndFestivalResponse(f.id, f.title)
    FROM Festival f
""")
    List<NewAndFestivalResponse> findAllFestivalTitle();
}
