package com.ra.base_spring_boot.security.Initaializer;

import com.ra.base_spring_boot.model.entity.movie.Genre;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.repository.IGenreRepository;
import com.ra.base_spring_boot.repository.IMovieHomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Order(3)
public class MovieGenreDataInitializer implements CommandLineRunner {

    private final IMovieHomeRepository movieHomeRepository;
    private final IGenreRepository genreRepository;

    @Transactional
    @Override
    public void run(String... args) {

        Movie laoHac = movieHomeRepository.findByTitleIgnoreCase("Lão Hạc").orElse(null);
        Movie soDo = movieHomeRepository.findByTitleIgnoreCase("Số đỏ").orElse(null);

        Genre hanhDong = genreRepository.findByGenreNameIgnoreCase("Hành Động").orElse(null);
        Genre haiHuoc = genreRepository.findByGenreNameIgnoreCase("Hài Hước").orElse(null);
        Genre tinhCam = genreRepository.findByGenreNameIgnoreCase("Tình Cảm").orElse(null);

        if (laoHac == null || soDo == null || hanhDong == null || haiHuoc == null || tinhCam == null) {
            return;
        }

        laoHac.getGenres().clear();
        laoHac.getGenres().add(tinhCam);
        soDo.getGenres().clear();
        soDo.getGenres().add(haiHuoc);

        movieHomeRepository.save(laoHac);
        movieHomeRepository.save(soDo);
    }
}
