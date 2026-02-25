package com.ra.base_spring_boot.model.entity.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.base_spring_boot.model.base.BaseObject;
import com.ra.base_spring_boot.model.constants.BookingStatus;
import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.theater.ShowTime;
import com.ra.base_spring_boot.model.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private Double totalPriceMovie; //co thaatj su can thiet?

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    private String qrCode;

    @Column(name = "booking_code",unique = true, nullable = false)
    private String bookingCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;

    // Quan hệ để lấy danh sách ghế đã đặt
    @OneToMany(mappedBy = "booking",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @Builder.Default
    private List<BookingSeat> bookingSeats = new ArrayList<>();

    // Method tính tổng tiền từ bookingSeats
    public Double calculateTotalAmount() {
        if (bookingSeats == null || bookingSeats.isEmpty()) return 0.0;
        return bookingSeats.stream()
                .mapToDouble(BookingSeat::getPrice)
                .sum();
    }

    // Tuỳ chọn: tính tổng số ghế
    public Integer calculateTotalSeat() {
        if (bookingSeats == null || bookingSeats.isEmpty()) return 0;
        return bookingSeats.stream()
                .mapToInt(BookingSeat::getQuantity)
                .sum();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = BookingStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}