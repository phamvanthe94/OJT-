package com.ra.base_spring_boot.security.Initializer;

import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.constants.SeatType;
import com.ra.base_spring_boot.model.entity.booking.TicketPrice;
import com.ra.base_spring_boot.repository.homerpo.ITicketPriceHomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class TicketPriceInitializer implements CommandLineRunner {

    private final ITicketPriceHomeRepository ticketPriceHomeRepository;

    @Override
    public void run(String... args) {

        if (ticketPriceHomeRepository.count() > 0)
            return;
        List<TicketPrice> ticketPriceList = List.of(
                TicketPrice.builder()
                        .typeSeat(SeatType.STANDARD)
                        .typeMovie(MovieType._2D)
                        .price(90d)
                        .dayType(false)
                        .startTime(LocalTime.of(9, 0))
                        .endTime(LocalTime.of(17, 59))
                        .build(),

                TicketPrice.builder()
                        .typeSeat(SeatType.VIP)
                        .typeMovie(MovieType._3D)
                        .price(180d)
                        .dayType(false)
                        .startTime(LocalTime.of(9, 0))
                        .endTime(LocalTime.of(17, 59))
                        .build()
        );

        ticketPriceHomeRepository.saveAll(ticketPriceList);

    }
}

