package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.FestivalRequest;
import com.ra.base_spring_boot.dto.resp.FestivalResponse;
import com.ra.base_spring_boot.exception.HttpBadRequest;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.entity.content.Festival;
import com.ra.base_spring_boot.repository.IFestivalRepository;
import com.ra.base_spring_boot.services.IFestivalService;
import com.ra.base_spring_boot.services.more.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalServiceImpl implements IFestivalService {

    private final IFestivalRepository festivalRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public Page<FestivalResponse> getAllFestivals(String search, int page, int size, String sortBy, String direction) {
        String sortField = sortBy == null || sortBy.isBlank() ? "id" : sortBy;
        Sort sort = "asc".equalsIgnoreCase(direction)
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), sort);
        String normalizedSearch = search == null || search.isBlank() ? null : search.trim();

        return festivalRepository.findByTitle(normalizedSearch, pageable).map(this::toResponse);
    }

    @Override
    public FestivalResponse createFestival(FestivalRequest request) {
        if (request.getImage() == null || request.getImage().isEmpty()) {
            throw new HttpBadRequest("Image cannot be null or empty");
        }

        Festival festival = Festival.builder()
                .title(request.getTitle())
                .image(cloudinaryService.upload(request.getImage()))
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        return toResponse(festivalRepository.save(festival));
    }

    @Override
    public FestivalResponse updateFestival(Long id, FestivalRequest request) {
        Festival oldFestival = festivalRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Festival not found"));

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            oldFestival.setImage(cloudinaryService.upload(request.getImage()));
        }

        oldFestival.setTitle(request.getTitle());
        oldFestival.setStartTime(request.getStartTime());
        oldFestival.setEndTime(request.getEndTime());

        return toResponse(festivalRepository.save(oldFestival));
    }

    @Override
    public void deleteFestival(Long id) {
        Festival festival = festivalRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Festival not found"));
        festivalRepository.delete(festival);
    }

    private FestivalResponse toResponse(Festival festival) {
        return FestivalResponse.builder()
                .id(festival.getId())
                .title(festival.getTitle())
                .image(festival.getImage())
                .startTime(festival.getStartTime())
                .endTime(festival.getEndTime())
                .build();
    }
}
