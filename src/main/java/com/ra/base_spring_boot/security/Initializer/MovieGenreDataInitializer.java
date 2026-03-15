package com.ra.base_spring_boot.security.Initializer;

import com.ra.base_spring_boot.model.entity.movie.Genre;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.repository.IGenreRepository;
import com.ra.base_spring_boot.repository.homerpo.IMovieHomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("!test")
@RequiredArgsConstructor
@Order(3)
public class MovieGenreDataInitializer implements CommandLineRunner {

    private final IMovieHomeRepository movieHomeRepository;
    private final IGenreRepository genreRepository;

    @Transactional
    @Override
    public void run(String... args) {
        Movie classicDrama = movieHomeRepository.findByTitleIgnoreCase("Classic Drama").orElse(null);
        Movie redCity = movieHomeRepository.findByTitleIgnoreCase("Red City").orElse(null);

        Genre romance = genreRepository.findByGenreNameIgnoreCase("Romance").orElse(null);
        Genre comedy = genreRepository.findByGenreNameIgnoreCase("Comedy").orElse(null);

        if (classicDrama == null || redCity == null || romance == null || comedy == null) {
            return;
        }

        classicDrama.getGenres().clear();
        classicDrama.getGenres().add(romance);
        redCity.getGenres().clear();
        redCity.getGenres().add(comedy);

        movieHomeRepository.save(classicDrama);
        movieHomeRepository.save(redCity);
    }
}
