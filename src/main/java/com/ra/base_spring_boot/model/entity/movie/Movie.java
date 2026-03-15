package com.ra.base_spring_boot.model.entity.movie;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ra.base_spring_boot.model.base.BaseObject;
import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.entity.theater.ShowTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MovieType type;

    @Column(name = "duration")
    private Integer duration;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MovieStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Release start date must not be null")
    @FutureOrPresent(message = "Release start date must be today or a future date")
    @Column(name = "release_start_date", nullable = false)
    private LocalDateTime releaseStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Release end date must not be null")
    @Column(name = "release_end_date", nullable = false)
    private LocalDateTime releaseEndDate;

    @AssertTrue(message = "Release end date must be greater than or equal to release start date")
    public boolean isValidReleasePeriod() {
        if (releaseStartDate == null || releaseEndDate == null) {
            return true;
        }
        return !releaseEndDate.isBefore(releaseStartDate);
    }

    @OneToMany(mappedBy = "movie")
    private List<ShowTime> showTimes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;
}
