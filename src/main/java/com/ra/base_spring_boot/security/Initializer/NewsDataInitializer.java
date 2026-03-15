package com.ra.base_spring_boot.security.Initializer;

import com.ra.base_spring_boot.model.entity.content.News;
import com.ra.base_spring_boot.repository.homerpo.INewsHomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class NewsDataInitializer implements CommandLineRunner {

    private final INewsHomeRepository newsHomeRepository;

    @Override
    public void run(String... args) {

        if (newsHomeRepository.count() > 0) {
            return;
        }

        List<News> newsList = List.of(
                News.builder()
                        .title("New blockbuster movie released this month")
                        .content("""
                                The newest action blockbuster has officially been released.
                                The movie features a strong cast and impressive visual effects.
                                It is expected to lead the box office for several weeks.
                                """)
                        .createdAt(LocalDateTime.now().minusDays(1))
                        .updatedAt(LocalDateTime.now().minusDays(1))
                        .build(),
                News.builder()
                        .title("International film festival coming soon")
                        .content("""
                                The international film festival will take place at the end of this month.
                                The event will feature outstanding movies from many countries.
                                It is a great opportunity for audiences to enjoy diverse cinema.
                                """)
                        .createdAt(LocalDateTime.now().minusDays(2))
                        .updatedAt(LocalDateTime.now().minusDays(2))
                        .build()
        );
        newsHomeRepository.saveAll(newsList);
    }
}
