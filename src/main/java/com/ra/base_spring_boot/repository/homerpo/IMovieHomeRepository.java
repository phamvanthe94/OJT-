package com.ra.base_spring_boot.repository.homerpo;

import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.entity.movie.Genre;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IMovieHomeRepository extends JpaRepository<Movie, Long> {

    // (1) Hiển thị phim đang chiếu / sắp chiếu (tùy status admin set)
    Page<Movie> findByStatus(MovieStatus status, Pageable pageable);

    // (2) Thể loại đang công chiếu (genres của các phim theo status)
    @Query("""
            SELECT DISTINCT g
            FROM Movie m
            JOIN m.genres g
            WHERE m.status = :status
            """)
    List<Genre> findGenresByMovieStatus(@Param("status") MovieStatus status);

    // (3) Hiển thị chi tiết phim đang chiếu (theo id + status) + load genres
    @Query("""
            SELECT m
            FROM Movie m
            LEFT JOIN FETCH m.genres
            WHERE m.id = :id
              AND m.status = :status
            """)
    Optional<Movie> findMovieDetailByIdAndStatus(
            @Param("id") Long id,
            @Param("status") MovieStatus status
    );

    // (Phụ) Tìm theo title để check trùng / search đơn giản
    Optional<Movie> findByTitleIgnoreCase(String title);

    // (4) Hiển thị phim sắp chiếu (về bản chất vẫn là status)
    default Page<Movie> findComingSoonMovies(MovieStatus status, Pageable pageable) {
        return findByStatus(status, pageable);
    }

    // (Phụ) Chi tiết phim (không filter status) + load genres
    @Query("""
            SELECT m
            FROM Movie m
            LEFT JOIN FETCH m.genres
            WHERE m.id = :id
            """)
    Optional<Movie> findMovieDetail(@Param("id") Long id);
}
