package com.ra.base_spring_boot.model.entity.theater;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.base_spring_boot.model.base.BaseObject;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "show_times")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ShowTime extends BaseObject {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
