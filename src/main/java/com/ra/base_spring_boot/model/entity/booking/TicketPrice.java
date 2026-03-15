package com.ra.base_spring_boot.model.entity.booking;

import com.ra.base_spring_boot.model.base.BaseObject;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.constants.SeatType;
import com.ra.base_spring_boot.model.entity.theater.ShowTime;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "ticket_prices")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TicketPrice extends BaseObject {

    @Enumerated(EnumType.STRING)
    @Column(name = "type_seat")
    private SeatType typeSeat;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_movie")
    private MovieType typeMovie;


    @Column(name = "price")
    private Double price;

    @Column(name = "day_type")
    private Boolean dayType;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;
}
