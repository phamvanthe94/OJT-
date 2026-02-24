package com.ra.base_spring_boot.repository.booking;

import com.ra.base_spring_boot.model.entity.theater.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IShowTimeRepository extends JpaRepository<ShowTime,Long> {
    @Query("""
    SELECT s FROM ShowTime s
    JOIN FETCH s.movie m
    JOIN FETCH s.screen sc
    JOIN FETCH sc.theater t
    WHERE m.id = :movieId
    AND s.startTime >= CURRENT_TIMESTAMP
    ORDER BY s.startTime
""")
    List<ShowTime> findShowTimeByMovie(@Param("movieId") Long movieId);

}
