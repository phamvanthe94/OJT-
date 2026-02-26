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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements IMovieService {

    private final IMovieRepository IMovieRepository;
    private final IGenreRepository IGenreRepository;
    private final CloudinaryService cloudinaryService;


    // get all (search + sort + page)
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
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        MovieType movieType = type != null ? MovieType.from(type) : null;
        MovieStatus movieStatus = status != null
                ? MovieStatus.valueOf(status.toUpperCase())
                : null;

        return IMovieRepository
                .search(title, author, movieType, movieStatus, pageable)
                .map(this::toResponse);
    }

    // Thêm mới
    @Override
    public MovieResponse createMovie(MovieRequest request) {

        String imageUrl = null;
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            imageUrl = cloudinaryService.upload(request.getImage());
        }

        Set<Genre> genres = request.getGenreIds() == null
                ? Set.of()
                : new HashSet<>(IGenreRepository.findAllById(request.getGenreIds()));

        Movie movie = Movie.builder()
                .title(request.getTitle())
                .descriptions(request.getDescriptions())
                .author(request.getAuthor())
                .image(imageUrl)
                .trailer(request.getTrailer())
                .type(request.getType())
                .duration(request.getDuration())
                .releaseDate(request.getReleaseDate())
                .releaseStartDate(request.getReleaseStartDate())
                .releaseEndDate(request.getReleaseEndDate())
                .status(request.getStatus())
                .genres(genres)
                .build();

        return toResponse(IMovieRepository.save(movie));
    }

    //Cập nhật
    @Override
    @Transactional
    public MovieResponse updateMovie(Long id, MovieRequest request) {

        Movie movie = IMovieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        movie.setTitle(request.getTitle());
        movie.setDescriptions(request.getDescriptions());
        movie.setAuthor(request.getAuthor());
        movie.setTrailer(request.getTrailer());
        movie.setType(request.getType());
        movie.setDuration(request.getDuration());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setReleaseStartDate(request.getReleaseStartDate());
        movie.setReleaseEndDate(request.getReleaseEndDate());
        movie.setStatus(request.getStatus());

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            movie.setImage(cloudinaryService.upload(request.getImage()));
        }

        if (request.getGenreIds() != null) {
            movie.setGenres(new HashSet<>(
                    IGenreRepository.findAllById(request.getGenreIds())
            ));
        }

        return toResponse(IMovieRepository.save(movie));
    }

    // Xoá
    @Override
    public void deleteMovie(Long id) {
        IMovieRepository.deleteById(id);
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
                .releaseStartDate(movie.getReleaseStartDate())
                .releaseEndDate(movie.getReleaseEndDate())
                .status(movie.getStatus())
                .genres(
                        movie.getGenres()
                                .stream()
                                .map(Genre::getGenreName)
                                .collect(Collectors.toSet())
                )
                .createdAt(movie.getCreatedAt())
                .updatedAt(movie.getUpdatedAt())
                .build();
    }
}
