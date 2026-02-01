package com.ra.base_spring_boot.services.homeService;

import com.ra.base_spring_boot.dto.resp.GenreResponse;

import java.util.List;

public interface IGenreHomeService {
    List<GenreResponse> getNowShowingGenres();
}
