package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.resp.ShowTimeListResponse;
import com.ra.base_spring_boot.repository.booking.IShowTimeRepository;
import com.ra.base_spring_boot.services.IShowTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ShowTimeServiceImp implements IShowTimeService {
    private final IShowTimeRepository showTimeRepository;
    @Override
    public List<ShowTimeListResponse> getShowTimesByMovie(Long movieId) {
        return showTimeRepository.findShowTimeByMovie(movieId)
                .stream()
                .map(ShowTimeListResponse::fromEntity)
                .toList();
    }
}
