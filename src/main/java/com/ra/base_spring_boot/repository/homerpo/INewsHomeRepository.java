package com.ra.base_spring_boot.repository.homerpo;

import com.ra.base_spring_boot.model.entity.content.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INewsHomeRepository extends JpaRepository<News, Long> {

    Page<News> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
