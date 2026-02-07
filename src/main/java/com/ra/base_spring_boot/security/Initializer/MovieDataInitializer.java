package com.ra.base_spring_boot.security.Initializer;

import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.repository.IMovieRepository;
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

    private final IMovieRepository movieRepository;
    List<Movie> comSoonMovies = List.of(
            Movie.builder()
                    .title("Chí Phèo")
                    .image("image3.jpg")
                    .trailer("example_trailer_link")
                    .author("Nam Cao")
                    .descriptions("Chí Phèo là một tác phẩm văn học nổi tiếng của nhà văn Nam Cao, kể về cuộc đời bi kịch của nhân vật Chí Phèo...")
                    .duration(110)
                    .releaseDate(LocalDate.of(2024, 12, 1))
                    .status(MovieStatus.COMING_SOON)
                    .type(MovieType._2D)
                    .build(),
            Movie.builder()
                    .title("Tuổi thơ dữ dội")
                    .image("image4.jpg")
                    .trailer("example_trailer_link")
                    .author("Phùng Quán")
                    .descriptions("Tuổi thơ dữ dội là một tiểu thuyết nổi tiếng của nhà văn Phùng Quán, kể về những năm tháng chiến tranh khốc liệt...")
                    .duration(95)
                    .releaseDate(LocalDate.of(2024, 11, 15))
                    .status(MovieStatus.COMING_SOON)
                    .type(MovieType._2D)
                    .build()
    );

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

                        .build()
        );
        movieRepository.saveAll(movies);
        movieRepository.saveAll(comSoonMovies);

    }
}
