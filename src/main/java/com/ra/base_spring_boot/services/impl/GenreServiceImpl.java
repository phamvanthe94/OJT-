package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.GenreRequest;
import com.ra.base_spring_boot.dto.resp.GenreResponse;
import com.ra.base_spring_boot.model.entity.movie.Genre;
import com.ra.base_spring_boot.repository.IGenreRepository;
import com.ra.base_spring_boot.services.IGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements IGenreService {

    private final IGenreRepository genreRepository;

    @Override
    public Page<GenreResponse> getAllGenres(String keyword, int page, int size, String sortBy, String direction) {

        String sortField = (sortBy == null || sortBy.isBlank()) ? "id" : sortBy;

        Sort sort = "desc".equalsIgnoreCase(direction) ?
                Sort.by(sortField).descending() :
                Sort.by(sortField).ascending();


        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                sort
        );

        Page<Genre> result = (keyword == null || keyword.isBlank()) ?
                genreRepository.findAll(pageable) :
                genreRepository.findByGenreNameContainingIgnoreCase(keyword.trim(), pageable);

        return result.map(genre -> GenreResponse.builder()
                .id(genre.getId())
                .genreName(genre.getGenreName())
                .build());
    }

    @Override
    public GenreResponse createGenre(GenreRequest genreName) {
        String name = genreName.getGenreName().trim();
        if (genreRepository.existsByGenreNameIgnoreCase(name)) {
            throw new RuntimeException("Tên thể loại phim đã tồn tại");
        }

        Genre genre = Genre.builder()
                .genreName(name)
                .build();

        Genre savedGenre = genreRepository.save(genre);

        return GenreResponse.builder()
                .id(savedGenre.getId())
                .genreName(savedGenre.getGenreName())
                .build();
    }

    @Transactional
    @Override
    public GenreResponse updateGenre(Long id, GenreRequest genreRequest) {

        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thể loại phim không tồn tại"));

        String newGenreName = genreRequest.getGenreName().trim();

        boolean newGenreNameExist = genreRepository.existsByGenreNameIgnoreCase(newGenreName);

        if (newGenreNameExist && !genre.getGenreName().equalsIgnoreCase(newGenreName)) {
            throw new RuntimeException("Tên thể loại phim đã tồn tại");
        }

        genre.setGenreName(newGenreName);

        Genre updatedGenre = genreRepository.save(genre);

        return GenreResponse.builder()
                .id(updatedGenre.getId())
                .genreName(updatedGenre.getGenreName())
                .build();
    }

    @Transactional
    @Override
    public void deleteGenre(Long id) {

        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thể loại phim không tồn tại"));

        genreRepository.delete(genre);

    }
}
