package com.ra.base_spring_boot.services.homeService.impl;

import com.ra.base_spring_boot.dto.resp.homeresp.FestivalDetailResponse;
import com.ra.base_spring_boot.dto.resp.homeresp.FestivalListResponse;
import com.ra.base_spring_boot.model.entity.content.Festival;
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

        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by("startTime").ascending()
        );

        return festivalHomeRepository.findAllFestivals(pageable)
                .map(this::toListResponse);
    }

    @Override
    public FestivalDetailResponse getFestivalDetail(Long id) {

        Festival festival = festivalHomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Festival not found"));

        return toDetailResponse(festival);
    }


    private FestivalListResponse toListResponse(Festival festival) {
        return FestivalListResponse.builder()
                .id(festival.getId())
                .title(festival.getTitle())
                .image(festival.getImage())
                .startTime(festival.getStartTime())
                .endTime(festival.getEndTime())
                .build();
    }

    private FestivalDetailResponse toDetailResponse(Festival festival) {
        return FestivalDetailResponse.builder()
                .id(festival.getId())
                .title(festival.getTitle())
                .image(festival.getImage())
                .startTime(festival.getStartTime())
                .endTime(festival.getEndTime())
                .build();
    }
}

