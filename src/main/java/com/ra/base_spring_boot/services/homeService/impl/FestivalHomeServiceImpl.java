package com.ra.base_spring_boot.services.homeService.impl;

import com.ra.base_spring_boot.dto.resp.homeresp.FestivalListResponse;
import com.ra.base_spring_boot.repository.homerpo.IFestivalHomeRepository;
import com.ra.base_spring_boot.services.homeService.IFestivalHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalHomeServiceImpl implements IFestivalHomeService {

    private final IFestivalHomeRepository festivalHomeRepository;

    @Override
    public Page<FestivalListResponse> getFestivals(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").descending());

        return festivalHomeRepository.findAllFestivals(pageable)
                .map(festival -> FestivalListResponse.builder()
                        .id(festival.getId())
                        .title(festival.getTitle())
                        .image(festival.getImage())
                        .startTime(festival.getStartTime())
                        .endTime(festival.getEndTime())
                        .build());
    }
}
