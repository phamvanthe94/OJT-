package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.entity.theater.ShowTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;


public interface IShowTimeAdminRepository extends JpaRepository<ShowTime, Long> {

    /**
     * Searches for ShowTime entities based on a keyword that matches the movie title, screen name,
     * or theater name.
     *
     * @param keyword
     * @param pageable
     * @return
     */
    @Query("""
            SELECT st
            FROM ShowTime st
            JOIN st.movie m
            JOIN st.screen s
            JOIN s.theater t
            WHERE (:keyword IS NULL OR :keyword = ''
               OR LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   )
            """)
    Page<ShowTime> searchShowTime(@Param("keyword") String keyword, Pageable pageable);


    /**
     * Fetches detailed information of a ShowTime by its ID, including associated Movie, Screen, and Theater entities.
     *
     * @param id the ID of the ShowTime
     * @return an Optional containing the ShowTime with its details if found, otherwise empty
     */
    @Query("""
            SELECT st
            FROM ShowTime st
            JOIN FETCH st.movie m
            JOIN FETCH st.screen s
            JOIN FETCH s.theater t
            WHERE st.id = :id
            """)
    Optional<ShowTime> findDetailById(@Param("id") Long id);


    /**
     * Checks if there are any overlapping showtimes for a given screen within a specified time range.
     *
     * @param screenId
     * @param excludeId
     * @param startTime
     * @param endTime
     * @return
     */
    @Query("""
            SELECT COUNT(st)> 0
            FROM ShowTime st
            WHERE st.screen.id = :screenId
                        AND(:excludeId IS NULL OR st.id <> :excludeId)
                                    AND(st.startTime < :endTime AND st.endTime > :startTime)
            """)
    boolean existsOverlapping(
            @Param("screenId") Long screenId,
            @Param("excludeId") Long excludeId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

}
