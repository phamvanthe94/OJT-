package com.ra.base_spring_boot.security.Initializer;

import com.ra.base_spring_boot.model.entity.content.News;
import com.ra.base_spring_boot.repository.homerpo.INewsHomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
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
                        .title("Ra mắt phim bom tấn tháng này ")
                        .content("""
                                Bom tấn hành động mới nhất đã chính thức ra mắt.
                                Phim quy tụ dàn diễn viên nổi tiếng và kỹ xảo hoành tráng.
                                Dự kiến sẽ dẫn đầu phòng vé trong nhiều tuần tới.
                                """)
                        .createdAt(LocalDateTime.now().minusDays(1))
                        .updatedAt(LocalDateTime.now().minusDays(1))
                        .build(),
                News.builder()
                        .title("Lễ hội phim quốc tế sắp diễn ra")
                        .content("""
                                Lễ hội phim quốc tế sẽ diễn ra vào cuối tháng này.
                                Sự kiện quy tụ nhiều bộ phim xuất sắc từ khắp nơi trên thế giới.
                                Đây là cơ hội tuyệt vời để thưởng thức nghệ thuật điện ảnh đa dạng.
                                """)
                        .createdAt(LocalDateTime.now().minusDays(2))
                        .updatedAt(LocalDateTime.now().minusDays(2))
                        .build()
        );
        newsHomeRepository.saveAll(newsList);
    }
}
