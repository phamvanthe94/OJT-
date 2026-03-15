package com.ra.base_spring_boot.security.Initializer;

import com.ra.base_spring_boot.model.entity.movie.Genre;
import com.ra.base_spring_boot.repository.IGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("!test")
@RequiredArgsConstructor
@Order(2)
public class GenreDataInitializer implements CommandLineRunner {

    private final IGenreRepository genreRepository;

    @Transactional
    @Override
    public void run(String... args) {
        if (genreRepository.count() > 0) {
            return;
        }

        saveIfNotExists("Action");
        saveIfNotExists("Adventure");
        saveIfNotExists("Comedy");
        saveIfNotExists("Horror");
        saveIfNotExists("Romance");
    }

    private void saveIfNotExists(String genreName) {
        if (!genreRepository.existsByGenreNameIgnoreCase(genreName)) {
            genreRepository.save(
                    Genre.builder()
                            .genreName(genreName)
                            .build()
            );
        }
    }
}
