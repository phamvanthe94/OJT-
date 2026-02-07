package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMovieRepository extends JpaRepository<Movie, Long> {

    @Query("""
                select m from Movie m
                where (:title is null or :title = '' or lower(m.title) like lower(concat('%', :title, '%')))
                  and (:author is null or :author = '' or lower(m.author) like lower(concat('%', :author, '%')))
                  and (:type is null or m.type = :type)
            """)
    Page<Movie> search(@Param("title") String title,
                       @Param("author") String author,
                       @Param("type") MovieType type,
                       Pageable pageable);

    @Query("""
                select m from Movie m
                where m.releaseDate <= CURRENT_DATE
            """)
    List<Movie> findNowShowing();

    @Query("""
                select m from Movie m
                where m.releaseDate > CURRENT_DATE
            """)
    List<Movie> findComingSoon();
}
