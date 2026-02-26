package com.ra.base_spring_boot.repository.booking;

import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.constants.SeatType;
import com.ra.base_spring_boot.model.entity.booking.TicketPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.Optional;

public interface ITicketPriceRepository extends JpaRepository<TicketPrice, Long> {
    @Query("SELECT t FROM TicketPrice t " +
            "WHERE (:dayType IS NULL OR t.dayType = :dayType) " +
            "AND (:seatType IS NULL OR t.typeSeat = :seatType) " +
            "AND (:movieType IS NULL OR t.typeMovie = :movieType)")
    Page<TicketPrice> search(@Param("dayType") Boolean dayType,
                             @Param("seatType") SeatType seatType,
                             @Param("movieType") MovieType movieType,
                             Pageable pageable);

    Optional<TicketPrice>
    findByTypeSeatAndTypeMovieAndDayTypeAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
    //findByShowTime_IdAndTypeSeatAndTypeMovieAndDayTypeAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            //Long showTimeId,
            SeatType typeSeat,
            MovieType typeMovie,
            Boolean dayType,
            LocalTime startTime,
            LocalTime endTime
    );
}

