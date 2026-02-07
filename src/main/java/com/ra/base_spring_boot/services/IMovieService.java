package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.req.MovieReq;
import com.ra.base_spring_boot.dto.resp.MovieResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMovieService {

    Page<MovieResp> findAll(Pageable pageable, String title, String author, String type);

    MovieResp findById(Long id);

    MovieResp create(MovieReq req);

    MovieResp update(Long id, MovieReq req);

    void delete(Long id);

    List<MovieResp> getNowShowing();

    List<MovieResp> getComingSoon();
}
