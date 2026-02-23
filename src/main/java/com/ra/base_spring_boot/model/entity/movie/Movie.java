package com.ra.base_spring_boot.model.entity.movie;

import com.ra.base_spring_boot.model.base.BaseObject;
import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.constants.MovieType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "movies")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Movie extends BaseObject {

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "descriptions", columnDefinition = "TEXT")
    private String descriptions;

    @Column(name = "author", length = 100)
    private String author;

    @Column(name = "image", length = 255)
    private String image;

    @Column(name = "trailer", length = 255)
    private String trailer;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MovieType type;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MovieStatus status;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;
}
