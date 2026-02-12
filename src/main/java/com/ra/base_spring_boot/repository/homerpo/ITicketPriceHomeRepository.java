package com.ra.base_spring_boot.repository.homerpo;

import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.constants.SeatType;
import com.ra.base_spring_boot.model.entity.booking.TicketPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface ITicketPriceHomeRepository extends JpaRepository<TicketPrice, Long> {

    @Query("""
            SELECT tp
            FROM TicketPrice tp
            WHERE (:typeSeat IS NULL OR tp.typeSeat = :typeSeat)
              AND (:typeMovie IS NULL OR tp.typeMovie = :typeMovie)
              AND (:dayType IS NULL OR tp.dayType = :dayType)
              AND (
                    :time IS NULL
                    OR (tp.startTime IS NOT NULL AND tp.endTime IS NOT NULL AND :time >= tp.startTime AND :time < tp.endTime)
                  )
            ORDER BY tp.dayType ASC, tp.typeMovie ASC, tp.typeSeat ASC, tp.startTime ASC
            """)
    List<TicketPrice> findTicketPriceBy(
            @Param("typeSeat") SeatType seatType,
            @Param("typeMovie") MovieType typeMovie,
            @Param("dayType") Boolean dayType,
            @Param("time") LocalTime time
    );
}
