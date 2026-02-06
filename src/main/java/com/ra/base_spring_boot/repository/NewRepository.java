package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.dto.resp.statisticResponse.IdTitleDTO;
import com.ra.base_spring_boot.model.entity.content.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewRepository extends JpaRepository<News, Long> {
    @Query("""
    select n from News n
    where (:title is null or n.title like %:title%)
    and (:content is null or n.content like %:content%)
    and (:festivalId is null or n.festival.id = :festivalId)
    """)
    Page<News> search(@Param("title") String title,
                      @Param("content") String content,
                      @Param("festivalId") Long festivalId,
                      Pageable pageable);
    @Query("SELECT COUNT(n) FROM News n")
    Long countNews();@Query("""
    SELECT new com.ra.base_spring_boot.dto.resp.statisticResponse.IdTitleDTO(n.id, n.title)
    FROM News n
""")
    List<IdTitleDTO> findAllNewsTitle();

}
