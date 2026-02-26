package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.MovieRequest;
import com.ra.base_spring_boot.dto.resp.MovieResponse;
import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.entity.movie.Genre;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.repository.IGenreRepository;
import com.ra.base_spring_boot.repository.IMovieRepository;
import com.ra.base_spring_boot.services.IMovieService;
import com.ra.base_spring_boot.services.more.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements IMovieService {

    private final IMovieRepository movieRepository;
    private final IGenreRepository genreRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public Page<MovieResponse> getAllMovies(
            String title,
            String author,
            String type,
            String status,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Pageable pageable = buildPageable(page, size, sortBy, direction);

        MovieType movieType = parseMovieType(type);       // query param String -> enum
        MovieStatus movieStatus = parseMovieStatus(status);

        Page<Movie> movies = movieRepository.search(
                normalizeBlankToNull(title),
                normalizeBlankToNull(author),
                movieType,
                movieStatus,
                pageable
        );

        return movies.map(this::toResponse);
    }

    @Override
    public MovieResponse createMovie(MovieRequest request) {
        validateCreate(request);

        Movie movie = toEntity(request);

        // upload ảnh nếu có
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String url = cloudinaryService.upload(request.getImage());
            movie.setImage(url);
        }

        Movie saved = movieRepository.save(movie);
        return toResponse(saved);
    }

    @Override
    public MovieResponse updateMovie(Long id, MovieRequest request) {
        Movie old = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Movie not found with id = " + id
                ));

        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MovieRequest is null");
        }

        // PATCH: client gửi field nào thì update field đó
        if (request.getTitle() != null) {
            String t = request.getTitle().trim();
            if (t.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title cannot be blank");
            old.setTitle(t);
        }

        if (request.getDescriptions() != null) {
            String d = request.getDescriptions().trim();
            if (d.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Descriptions cannot be blank");
            old.setDescriptions(d);
        }

        if (request.getAuthor() != null) {
            String a = request.getAuthor().trim();
            if (a.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author cannot be blank");
            old.setAuthor(a);
        }

        if (request.getTrailer() != null) {
            String tr = request.getTrailer().trim();
            if (tr.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trailer cannot be blank");
            old.setTrailer(tr);
        }

        if (request.getType() != null) {
            old.setType(request.getType());
        }

        if (request.getDuration() != null) {
            if (request.getDuration() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duration must be > 0");
            }
            old.setDuration(request.getDuration());
        }

        if (request.getReleaseDate() != null) {
            old.setReleaseDate(request.getReleaseDate());
        }

        if (request.getReleaseStartDate() != null) {
            old.setReleaseStartDate(request.getReleaseStartDate());
        }

        if (request.getReleaseEndDate() != null) {
            old.setReleaseEndDate(request.getReleaseEndDate());
        }

        if (request.getStatus() != null) {
            old.setStatus(request.getStatus());
        }

        // genres: chỉ update nếu client có gửi genreIds
        if (request.getGenreIds() != null) {
            Set<Genre> genres = genreRepository.findAllById(request.getGenreIds())
                    .stream()
                    .collect(Collectors.toSet());
            old.setGenres(genres);
        }

        // image: có file mới thì upload, không thì giữ cũ
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String url = cloudinaryService.upload(request.getImage());
            old.setImage(url);
        }

        Movie updated = movieRepository.save(old);
        return toResponse(updated);
    }

    @Override
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Movie not found with id = " + id
                ));
        movieRepository.delete(movie);
    }

    // ================= Helpers =================

    private Pageable buildPageable(int page, int size, String sortBy, String direction) {
        int safePage = Math.max(page, 0);
        int safeSize = (size <= 0) ? 10 : Math.min(size, 200);

        String safeSortBy = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy.trim();
        Sort sort = Sort.by(safeSortBy);

        if (direction != null && direction.equalsIgnoreCase("DESC")) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        return PageRequest.of(safePage, safeSize, sort);
    }

    private void validateCreate(MovieRequest req) {
        if (req == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MovieRequest is null");

        // MovieRequest đã có @NotBlank/@NotNull, nhưng service vẫn check để an toàn khi gọi internal
        if (req.getTitle() == null || req.getTitle().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title cannot be blank");

        if (req.getDescriptions() == null || req.getDescriptions().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Descriptions cannot be blank");

        if (req.getAuthor() == null || req.getAuthor().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author cannot be blank");

        if (req.getTrailer() == null || req.getTrailer().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trailer cannot be blank");

        if (req.getType() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type cannot be blank");

        if (req.getDuration() == null || req.getDuration() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duration must be > 0");

        if (req.getReleaseStartDate() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Release start date cannot be null");

        if (req.getReleaseEndDate() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Release end date cannot be null");

        if (req.getStatus() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status cannot be null");
    }

    private Movie toEntity(MovieRequest req) {
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
                .type(req.getType())
                .duration(req.getDuration())
                .releaseDate(req.getReleaseDate())
                .releaseStartDate(req.getReleaseStartDate())
                .releaseEndDate(req.getReleaseEndDate())
                .status(req.getStatus())
                .genres(genres)
                .build();
    }

    private MovieResponse toResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .descriptions(movie.getDescriptions())
                .author(movie.getAuthor())
                .image(movie.getImage())
                .trailer(movie.getTrailer())
                .type(movie.getType())
                .duration(movie.getDuration())
                .releaseDate(movie.getReleaseDate())
                .status(movie.getStatus())
                .releaseStartDate(movie.getReleaseStartDate())
                .releaseEndDate(movie.getReleaseEndDate())
                .genres(movie.getGenres() == null ? Set.of()
                        : movie.getGenres().stream()
                        .map(Genre::getGenreName)
                        .collect(Collectors.toSet()))
                .createdAt(movie.getCreatedAt())
                .updatedAt(movie.getUpdatedAt())
                .build();
    }

    private String normalizeBlankToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isBlank() ? null : t;
    }

    private MovieType parseMovieType(String type) {
        if (type == null || type.isBlank()) return null;
        String t = type.trim();

        // chấp nhận "2D", "3D", "_2D", "_3D" (tùy enum bạn define)
        try {
            if (t.startsWith("_")) return MovieType.valueOf(t);
            return MovieType.from(t); // nếu enum MovieType có from("2D") -> _2D
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid MovieType: " + type);
        }
    }

    private MovieStatus parseMovieStatus(String status) {
        if (status == null || status.isBlank()) return null;
        String s = status.trim().toUpperCase();
        try {
            return MovieStatus.valueOf(s);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid MovieStatus: " + status);
        }
    }
}