package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.constants.SeatType;
import com.ra.base_spring_boot.model.entity.booking.TicketPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface ITicketPriceHomeRepository extends JpaRepository<TicketPrice, Long> {

    /*
     * Find ticket prices based on seat type, movie type, day type, and time.
     * @param seatType The type of seat (e.g., REGULAR, VIP). Can be null to ignore this filter.
     * @param movieType The type of movie (e.g., 2D, 3D). Can be null to ignore this filter
     * @param dayType The type of day (false for Mon-Thu, true for Fri-Sun and holidays). Can be null to ignore this filter.
     * @param time The specific time to check against the ticket price's start and end times
     * @return A list of TicketPrice entities that match the specified criteria, ordered by day type, movie type, seat type, and start time.
     *
     * */
    @Query("""
            SELECT tp
            FROM TicketPrice tp
            WHERE (:typeSeat IS NULL OR tp.typeSeat = :typeSeat)
            AND (:typeMovie IS NULL OR tp.typeMovie = :typeMovie)
            AND (:dayType IS NULL OR tp.dayType = :dayType)
            AND (:time IS NULL
            OR (tp.startTime IS NULL OR tp.endTime IS NULL )
            OR (:time >= tp.startTime AND :time <= tp.endTime))
            ORDER BY tp.dayType ASC , tp.typeMovie ASC , tp.typeSeat ASC,tp.startTime ASC
            """)
    List<TicketPrice> findTicketPriceBy(
            @Param("typeSeat") SeatType seatType,
            @Param("typeMovie") MovieType typeMovie,
            @Param("dayType") boolean dayType,
            @Param("time") LocalTime time
    );
}
