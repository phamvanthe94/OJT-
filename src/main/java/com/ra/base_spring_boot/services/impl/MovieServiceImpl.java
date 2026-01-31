package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.MovieDTO;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.repository.MovieRepository;
import com.ra.base_spring_boot.services.CloudinaryService;
import com.ra.base_spring_boot.services.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class MovieServiceImpl implements IMovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public ResponseEntity<ResponseWrapper<Page<Movie>>> getAllMovie(
            String title,
            String author,
            String type,
            Pageable pageable
    ) {
        Page<Movie> moviePage = movieRepository.search(title, author, type, pageable);

        ResponseWrapper<Page<Movie>> responseWrapper = ResponseWrapper.<Page<Movie>>builder()
                .data(moviePage)
                .code(200)
                .status(HttpStatus.OK)
                .build();

        return ResponseEntity.ok(responseWrapper);
    }

    @Override
    public ResponseEntity<ResponseWrapper<?>> createMovie(MovieDTO movieDTO, BindingResult bindingResult) {
        // Validate image
        if (movieDTO.getImage() == null || movieDTO.getImage().isEmpty()) {
            ResponseWrapper<String> error = ResponseWrapper.<String>builder()
                    .data("Image cannot be null or empty")
                    .code(400)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Movie movie = convertMovieDTOToMovie(movieDTO);

        String urlImage = cloudinaryService.upload(movieDTO.getImage());
        movie.setImage(urlImage);

        Movie newMovie = movieRepository.save(movie);

        ResponseWrapper<Movie> responseWrapper = ResponseWrapper.<Movie>builder()
                .data(newMovie)
                .code(201)
                .status(HttpStatus.CREATED)
                .build();

        return new ResponseEntity<>(responseWrapper, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseWrapper<?>> updateMovie(Long id, MovieDTO movieDTO) {
        Movie oldMovie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));

        if (movieDTO == null) {
            ResponseWrapper<String> error = ResponseWrapper.<String>builder()
                    .data("MovieDTO is null")
                    .code(400)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Movie movie = convertMovieDTOToMovie(movieDTO);
        movie.setId(oldMovie.getId());

        // nếu có ảnh mới -> upload, không có -> giữ ảnh cũ
        if (movieDTO.getImage() != null && !movieDTO.getImage().isEmpty()) {
            String urlImage = cloudinaryService.upload(movieDTO.getImage());
            movie.setImage(urlImage);
        } else {
            movie.setImage(oldMovie.getImage());
        }

        Movie updatedMovie = movieRepository.save(movie);

        ResponseWrapper<Movie> responseWrapper = ResponseWrapper.<Movie>builder()
                .data(updatedMovie)
                .code(200)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseWrapper<String>> deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));

        movieRepository.delete(movie);

        ResponseWrapper<String> responseWrapper = ResponseWrapper.<String>builder()
                .data("Delete movie successfully")
                .code(200)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @Override
    public Movie convertMovieDTOToMovie(MovieDTO movieDTO) {
        MovieType movieType = null;

        if (movieDTO.getType() != null && !movieDTO.getType().isBlank()) {
            try {
                movieType = MovieType.from(movieDTO.getType().trim());
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException("Invalid Movie Type: " + movieDTO.getType());
            }
        }

        return Movie.builder()
                .title(movieDTO.getTitle())
                .descriptions(movieDTO.getDescriptions())
                .author(movieDTO.getAuthor())
                .trailer(movieDTO.getTrailer())
                .type(movieType)
                .duration(movieDTO.getDuration())
                .releaseDate(movieDTO.getReleaseDate())
                .createdAt(movieDTO.getCreatedAt())
                .updatedAt(movieDTO.getUpdatedAt())
                .build();
    }

}
