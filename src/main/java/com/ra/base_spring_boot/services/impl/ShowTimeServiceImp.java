package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.resp.ShowTimeListResponse;
import com.ra.base_spring_boot.repository.IShowTimeRepository;
import com.ra.base_spring_boot.services.IShowTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ShowTimeServiceImp implements IShowTimeService {
    @Autowired
    IShowTimeRepository showTimeRepository;
    @Override
    public List<ShowTimeListResponse> getShowTimesByMovie(Long movieId) {
        return showTimeRepository.findShowTimeByMovie(movieId)
                .stream()
                .map(ShowTimeListResponse::fromEntity)
                .toList();
    }
}
