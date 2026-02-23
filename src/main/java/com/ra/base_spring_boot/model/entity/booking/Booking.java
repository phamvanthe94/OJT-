package com.ra.base_spring_boot.model.entity.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.base_spring_boot.model.base.BaseObject;
import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.theater.ShowTime;
import com.ra.base_spring_boot.model.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Booking extends BaseObject {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private ShowTime showTime;

    @Column(name = "total_seat")
    private Integer totalSeat;

    @Column(name = "total_price_movie")
    private Double totalPriceMovie;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;


    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY)
    private List<Payment> payments;


}
