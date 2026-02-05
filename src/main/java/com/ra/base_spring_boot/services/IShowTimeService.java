package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.resp.ShowTimeListResponse;

import java.util.List;

public interface IShowTimeService {
    public List<ShowTimeListResponse> getShowTimesByMovie(Long movieId);
}
