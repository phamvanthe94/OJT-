package com.ra.base_spring_boot.security.Initaializer;

import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(1)
public class MovieDataInitializer implements CommandLineRunner {

    private final MovieRepository movieRepository;

    @Transactional
    @Override
    public void run(String... args) {
        if (movieRepository.count() > 0)
            return;

        List<Movie> movies = List.of(
                Movie.builder()
                        .title("Lão Hạc")
                        .image("image1.jpg")
                        .trailer("example_trailer_link")
                        .author("Nam Cao")
                        .descriptions("Lão Hạc là một truyện ngắn nổi tiếng của nhà văn Nam Cao, kể về cuộc sống và số phận của một người nông dân nghèo tên là Lão Hạc...")
                        .duration(120)
                        .releaseDate(LocalDate.of(2024, 1, 1))
                        .status(MovieStatus.NOW_SHOWING)
                        .type(MovieType._2D)
                        .createdAt(LocalDate.now())
                        .build(),

                Movie.builder()
                        .title("Số đỏ")
                        .image("image2.jpg")
                        .trailer("example_trailer_link")
                        .author("Vũ Trọng Phụng")
                        .descriptions("Số đỏ là một tiểu thuyết châm biếm xã hội Việt Nam trong thập niên 1930.")
                        .duration(90)
                        .releaseDate(LocalDate.of(2024, 2, 1))
                        .status(MovieStatus.NOW_SHOWING)
                        .type(MovieType._2D)
                        .createdAt(LocalDate.now())
                        .build()
        );
        movieRepository.saveAll(movies);

    }
}
