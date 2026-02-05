package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.MovieDTO;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.repository.MovieRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    public ResponseEntity<ResponseWrapper<Page<Movie>>> getAllMovie(String title, String author, String type, Pageable pageable) {
        Page<Movie> moviePage = movieRepository.search(title, author, type, pageable);
        ResponseWrapper<Page<Movie>> responseWrapper = ResponseWrapper.<Page<Movie>>builder()
                .data(moviePage)
                .code(200)
                .status(org.springframework.http.HttpStatus.OK)
                .build();
        return ResponseEntity.ok(responseWrapper);
    }

    public ResponseEntity<ResponseWrapper<?>> createMovie(@Valid @ModelAttribute MovieDTO movieDTO, BindingResult bindingResult) {
        if (movieDTO.getImage() == null || movieDTO.getImage().isEmpty()) {
            bindingResult.rejectValue("image", "400", "Image cannot be null or empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Movie movie = convertMovieDTOToMovie(movieDTO);
            String urlImage = cloudinaryService.upload(movieDTO.getImage());
            movie.setImage(urlImage);
            Movie newMovie = movieRepository.save(movie);
            ResponseWrapper<Movie> responseWrapper = ResponseWrapper
                    .<Movie>builder()
                    .data(newMovie)
                    .code(201)
                    .status(HttpStatus.CREATED)
                    .build();
            return new ResponseEntity<>(responseWrapper,HttpStatus.CREATED);
        }
    }
    public ResponseEntity<ResponseWrapper<?>> updateMovie(Long id, MovieDTO movieDTO) {
        Movie oldMovie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
        if (movieDTO != null) {
            Movie movie = convertMovieDTOToMovie(movieDTO);
            movie.setId(oldMovie.getId());
            if (movieDTO.getImage() != null && !movieDTO.getImage().isEmpty()) {
                String urlImage = cloudinaryService.upload(movieDTO.getImage());
                movie.setImage(urlImage);
            } else {
                movie.setImage(oldMovie.getImage());
            }
            Movie updatedMovie = movieRepository.save(movie);
            ResponseWrapper<Movie> responseWrapper = ResponseWrapper
                    .<Movie>builder()
                    .data(updatedMovie)
                    .code(200)
                    .status(HttpStatus.OK)
                    .build();
            return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
        } else {
            throw new RuntimeException("MovieDTO is null");
        }
    }
    public ResponseEntity<ResponseWrapper<String>> deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
        movieRepository.delete(movie);
        ResponseWrapper<String> responseWrapper = ResponseWrapper
                .<String>builder()
                .data("Delete movie successfully")
                .code(200)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

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
                .releaseDate(movieDTO.getReleaseDate() == null ? null : movieDTO.getReleaseDate().atStartOfDay())
                .createdAt(movieDTO.getCreatedAt() == null ? null : movieDTO.getCreatedAt().atStartOfDay())
                .updatedAt(movieDTO.getUpdatedAt() == null ? null : movieDTO.getUpdatedAt().atStartOfDay())
                .build();
    }
    public List<MovieDTO> getNowShowing() {
        return movieRepository.findNowShowing()
                .stream()
                .map(movie -> MovieDTO.builder()
                        .id(movie.getId())
                        .title(movie.getTitle())
                        .duration(movie.getDuration())
                        .releaseDate(movie.getReleaseDate())
                        // image: KHÔNG set MultipartFile
                        // nếu muốn trả ảnh thì thêm field imageUrl (String)
                        .build()
                )
                .toList();
    }
}
