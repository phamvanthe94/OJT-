package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.MovieReq;
import com.ra.base_spring_boot.dto.resp.MovieResp;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.entity.movie.Genre;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.repository.IGenreRepository;
import com.ra.base_spring_boot.repository.IMovieRepository;
import com.ra.base_spring_boot.services.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements IMovieService {

    @Autowired
    private IMovieRepository movieRepository;

    @Autowired
    private IGenreRepository genreRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public Page<MovieResp> findAll(Pageable pageable, String title, String author, String type) {
        // type ở query param là String -> convert sang enum để search
        MovieType movieType = parseMovieTypeQuery(type);

        Page<Movie> movies = movieRepository.search(title, author, movieType, pageable);
        return movies.map(this::toResp);
    }

    @Override
    public MovieResp findById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Movie not found with id = " + id
                ));
        return toResp(movie);
    }

    @Override
    public MovieResp create(MovieReq req) {
        if (req == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MovieReq is null");
        }
        if (req.getTitle() == null || req.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title không được để trống");
        }
        if (req.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status không được để trống");
        }
        if (req.getType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type không được để trống");
        }
        if (req.getDuration() == null || req.getDuration() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duration phải > 0");
        }
        if (req.getReleaseDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ReleaseDate không được để trống");
        }


        Movie movie = toEntity(req);

        // upload ảnh nếu có
        if (req.getImage() != null && !req.getImage().isEmpty()) {
            String url = cloudinaryService.upload(req.getImage());
            movie.setImage(url);
        }

        Movie saved = movieRepository.save(movie);
        return toResp(saved);
    }

    @Override
    public MovieResp update(Long id, MovieReq req) {
        Movie old = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Movie not found with id = " + id
                ));

        if (req == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MovieReq is null");
        }
        if (req.getTitle() == null || req.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title không được để trống");
        }
        if (req.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status không được để trống");
        }
        if (req.getType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type không được để trống");
        }
        if (req.getDuration() == null || req.getDuration() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duration phải > 0");
        }
        if (req.getReleaseDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ReleaseDate không được để trống");
        }

        Movie movie = toEntity(req);
        movie.setId(id);

        // giữ ảnh cũ nếu không upload ảnh mới
        if (req.getImage() != null && !req.getImage().isEmpty()) {
            String url = cloudinaryService.upload(req.getImage());
            movie.setImage(url);
        } else {
            movie.setImage(old.getImage());
        }

        Movie updated = movieRepository.save(movie);
        return toResp(updated);
    }

    @Override
    public void delete(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Movie not found with id = " + id
                ));
        movieRepository.delete(movie);
    }

    @Override
    public List<MovieResp> getNowShowing() {
        return movieRepository.findNowShowing()
                .stream()
                .map(this::toResp)
                .toList();
    }

    @Override
    public List<MovieResp> getComingSoon() {
        return movieRepository.findComingSoon()
                .stream()
                .map(this::toResp)
                .toList();
    }

    // ================== HELPER ==================

    private Movie toEntity(MovieReq req) {

        Set<Genre> genres = new HashSet<>();
        if (req.getGenreIds() != null && !req.getGenreIds().isEmpty()) {
            genres = genreRepository.findAllById(req.getGenreIds())
                    .stream()
                    .collect(Collectors.toSet());
        }

        return Movie.builder()
                .title(req.getTitle())
                .descriptions(req.getDescriptions())
                .author(req.getAuthor())
                .trailer(req.getTrailer())
                .type(req.getType())          // ✅ enum
                .duration(req.getDuration())
                .releaseDate(req.getReleaseDate())
                .status(req.getStatus())      // ✅ enum
                .genres(genres)
                .build();
    }

    private MovieResp toResp(Movie movie) {
        return MovieResp.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .descriptions(movie.getDescriptions())
                .author(movie.getAuthor())
                .image(movie.getImage())
                .trailer(movie.getTrailer())
                .type(movie.getType() == null ? null : movie.getType().toValue()) // 2D/3D đẹp
                .duration(movie.getDuration())
                .releaseDate(movie.getReleaseDate())
                .status(movie.getStatus() == null ? null : movie.getStatus().name())
                .genres(movie.getGenres() == null ? Set.of()
                        : movie.getGenres().stream()
                        .map(Genre::getGenreName)
                        .collect(Collectors.toSet()))
                .createdAt(movie.getCreatedAt())
                .updatedAt(movie.getUpdatedAt())
                .build();
    }

    // type query param đang là String -> convert sang enum để search
    // chấp nhận: "2D", "3D", "_2D", "_3D"
    private MovieType parseMovieTypeQuery(String type) {
        if (type == null || type.isBlank()) return null;

        String t = type.trim();
        try {
            if (t.startsWith("_")) {
                return MovieType.valueOf(t);
            }
            // MovieType.from("2D") -> _2D
            return MovieType.from(t);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid MovieType: " + type);
        }
    }
}
