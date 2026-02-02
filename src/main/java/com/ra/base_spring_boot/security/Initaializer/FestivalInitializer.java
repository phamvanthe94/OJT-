package com.ra.base_spring_boot.security.Initaializer;

import com.ra.base_spring_boot.model.entity.content.Festival;
import com.ra.base_spring_boot.repository.homerpo.IFestivalHomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
                        .startTime(java.time.LocalDate.of(2024, 3, 20))
                        .endTime(java.time.LocalDate.of(2024, 3, 30))
                        .build(),
                Festival.builder()
                        .title("Summer Festival")
                        .image("summer_festival.jpg")
                        .startTime(java.time.LocalDate.of(2024, 6, 15))
                        .endTime(java.time.LocalDate.of(2024, 6, 25))
                        .build(),
                Festival.builder()
                        .title("Autumn Festival")
                        .image("autumn_festival.jpg")
                        .startTime(java.time.LocalDate.of(2024, 9, 10))
                        .endTime(java.time.LocalDate.of(2024, 9, 20))
                        .build(),
                Festival.builder()
                        .title("Winter Festival")
                        .image("winter_festival.jpg")
                        .startTime(java.time.LocalDate.of(2024, 12, 5))
                        .endTime(java.time.LocalDate.of(2024, 12, 15))
                        .build()
        );
        festivalHomeRepository.saveAll(festivals);
    }
}
