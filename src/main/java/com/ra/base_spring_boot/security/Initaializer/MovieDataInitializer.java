package com.ra.base_spring_boot.security.Initaializer;

import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MovieDataInitializer implements CommandLineRunner {

    private final MovieRepository movieRepository;

    @Transactional
    @Override
    public void run(String... args) {
        if (movieRepository.count() > 0)
            return;

        List<Movie> seeMovies = List.of(
                Movie.builder()
                        .title("Lão Hạc")
                        .descriptions("Câu chuyện cảm động về tình cha con và sự hy sinh của người nông dân nghèo Lão Hạc.")
                        .author("Nam Cao")
                        .image("https://example.com/images/lao_hac.jpg")
                        .trailer("https://example.com/trailers/lao_hac.mp4")
                        .duration(181)
                        .releaseDate(LocalDate.of(2020, 5, 20))
                        .status(MovieStatus.NOW_SHOWING)
                        .build(),

                Movie.builder()
                        .title("Chí Phèo")
                        .descriptions("Tác phẩm kinh điển về cuộc đời bi kịch của Chí Phèo trong xã hội phong kiến.")
                        .author("Nam Cao")
                        .image("https://example.com/images/chi_pheo.jpg")
                        .trailer("https://example.com/trailers/chi_pheo.mp4")
                        .duration(150)
                        .releaseDate(LocalDate.of(2019, 8, 15))
                        .status(MovieStatus.NOW_SHOWING)
                        .build(),

                Movie.builder()
                        .title("Số Đỏ")
                        .descriptions("Câu chuyện hài hước về cuộc sống và những mảng đen tối của xã hội qua lăng kính của nhân vật Xã Xệ.")
                        .author("Vũ Trọng Phụng")
                        .image("https://example.com/images/so_do.jpg")
                        .trailer("https://example.com/trailers/so_do.mp4")
                        .duration(200)
                        .releaseDate(LocalDate.of(2021, 11, 10))
                        .status(MovieStatus.NOW_SHOWING)
                        .build()
        );

    }
}
