package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.entity.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("""
            select m from Movie m 
            where (:title is null or m.title like %:title%)
            and (:author is null or m.author like %:author%)
            and (:type is null or m.type = :type)
            """)
    Page<Movie> search(@Param("title") String title,
                       @Param("author") String author,
                       @Param("type") String type,
                       Pageable pageable);


}
