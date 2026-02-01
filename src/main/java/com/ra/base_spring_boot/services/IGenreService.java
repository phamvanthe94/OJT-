package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.req.GenreRequest;
import com.ra.base_spring_boot.dto.resp.GenreResponse;
import org.springframework.data.domain.Page;

public interface IGenreService {

    Page<GenreResponse> getAllGenres(String keyword, int page, int size, String sortBy, String direction);

    GenreResponse createGenre(String genreName);

    GenreResponse updateGenre(Long id, GenreRequest genreRequest);

    void deleteGenre(Long id);

}
