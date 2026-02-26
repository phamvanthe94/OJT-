package com.ra.base_spring_boot.security.Initializer;

import com.ra.base_spring_boot.model.entity.content.Festival;
import com.ra.base_spring_boot.repository.homerpo.IFestivalHomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FestivalInitializer implements CommandLineRunner {

    private final IFestivalHomeRepository festivalHomeRepository;

    @Override
    public void run(String... args) {
        if (festivalHomeRepository.count() > 0) {
            return;
        }

        List<Festival> festivals = List.of(
                Festival.builder()
                        .title("Spring Festival")
                        .image("spring_festival.jpg")
                        .startTime(LocalDateTime.of(2024, 3, 20, 0, 0))
                        .endTime(LocalDateTime.of(2024, 3, 30, 23, 59))
                        .build(),
                Festival.builder()
                        .title("Summer Festival")
                        .image("summer_festival.jpg")
                        .startTime(LocalDateTime.of(2024, 6, 15, 0, 0))
                        .endTime(LocalDateTime.of(2024, 6, 25, 23, 59))
                        .build(),
                Festival.builder()
                        .title("Autumn Festival")
                        .image("autumn_festival.jpg")
                        .startTime(LocalDateTime.of(2024, 9, 10, 0, 0))
                        .endTime(LocalDateTime.of(2024, 9, 20, 23, 59))
                        .build(),
                Festival.builder()
                        .title("Winter Festival")
                        .image("winter_festival.jpg")
                        .startTime(LocalDateTime.of(2024, 12, 5, 0, 0))
                        .endTime(LocalDateTime.of(2024, 12, 15, 23, 59))
                        .build()
        );
        festivalHomeRepository.saveAll(festivals);
    }
}
