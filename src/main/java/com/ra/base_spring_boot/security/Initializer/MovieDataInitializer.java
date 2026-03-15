package com.ra.base_spring_boot.security.Initializer;

import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.repository.IMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("!test")
@RequiredArgsConstructor
@Order(1)
public class MovieDataInitializer implements CommandLineRunner {

    private final IMovieRepository movieRepository;

    @Transactional
    @Override
    public void run(String... args) {
        if (movieRepository.count() > 0) {
            return;
        }

        List<Movie> movies = List.of(
                movie("Classic Drama", "Demo Author A", MovieStatus.NOW_SHOWING, MovieType._2D, 120, 1, 30),
                movie("Red City", "Demo Author B", MovieStatus.NOW_SHOWING, MovieType._2D, 90, 1, 30),
                movie("Village Story", "Demo Author C", MovieStatus.COMING_SOON, MovieType._2D, 110, 31, 60),
                movie("Youth Journey", "Demo Author D", MovieStatus.COMING_SOON, MovieType._2D, 95, 31, 60)
        );

        movieRepository.saveAll(movies);
    }

    private Movie movie(
            String title,
            String author,
            MovieStatus status,
            MovieType type,
            int duration,
            int startAfterDays,
            int endAfterDays
    ) {
        LocalDateTime startDate = LocalDateTime.now().plusDays(startAfterDays);
        return Movie.builder()
                .title(title)
                .image("image-" + title.toLowerCase().replace(" ", "-") + ".jpg")
                .trailer("example_trailer_link")
                .author(author)
                .descriptions("Seed movie for local testing")
                .duration(duration)
                .releaseDate(startDate)
                .releaseStartDate(startDate)
                .releaseEndDate(LocalDateTime.now().plusDays(endAfterDays))
                .status(status)
                .type(type)
                .build();
    }
}
