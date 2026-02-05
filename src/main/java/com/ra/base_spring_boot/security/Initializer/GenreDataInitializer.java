package com.ra.base_spring_boot.security.Initializer;

import com.ra.base_spring_boot.model.entity.movie.Genre;
import com.ra.base_spring_boot.repository.homerpo.IGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Order(2)
public class GenreDataInitializer implements CommandLineRunner {

    private final IGenreRepository genreRepository;

    @Transactional
    @Override
    public void run(String... args) {
        if (genreRepository.count() > 0)
            return;

        saveIfNotExists("Hành Động");
        saveIfNotExists("Phiêu Lưu");
        saveIfNotExists("Hài Hước");
        saveIfNotExists("Kinh Dị");
        saveIfNotExists("Tình Cảm");
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
