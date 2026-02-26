package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.FestivalRequest;
import com.ra.base_spring_boot.dto.resp.FestivalResponse;
import com.ra.base_spring_boot.model.entity.content.Festival;
import com.ra.base_spring_boot.repository.IFestivalRepository;
import com.ra.base_spring_boot.services.more.CloudinaryService;
import com.ra.base_spring_boot.services.IFestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalServiceImpl implements IFestivalService {

    private final IFestivalRepository festivalRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public Page<FestivalResponse> getAllFestivals(
            String search,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        String sortField = (sortBy == null || sortBy.isBlank()) ? "id" : sortBy;

        Sort sort = "asc".equalsIgnoreCase(direction)
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                sort
        );

        // Normalize search: treat null or blank as null so repository query doesn't match all records
        String normalizedSearch = (search == null || search.isBlank()) ? null : search.trim();

        Page<Festival> festivals = festivalRepository.findByTitle(normalizedSearch, pageable);

        return festivals.map(this::toResponse);
    }

    @Override
    public FestivalResponse createFestival(FestivalRequest request) {

        if (request.getImage() == null || request.getImage().isEmpty()) {
            throw new RuntimeException("Image cannot be null or empty");
        }

        String imageUrl = cloudinaryService.upload(request.getImage());

        Festival festival = Festival.builder()
                .title(request.getTitle())
                .image(imageUrl)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        return toResponse(festivalRepository.save(festival));
    }

    @Override
    public FestivalResponse updateFestival(Long id, FestivalRequest request) {

        Festival oldFestival = festivalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Festival không tồn tại"));

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageUrl = cloudinaryService.upload(request.getImage());
            oldFestival.setImage(imageUrl);
        }

        oldFestival.setTitle(request.getTitle());
        oldFestival.setStartTime(request.getStartTime());
        oldFestival.setEndTime(request.getEndTime());

        return toResponse(festivalRepository.save(oldFestival));
    }

    @Override
    public void deleteFestival(Long id) {
        Festival festival = festivalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Festival không tồn tại"));
        festivalRepository.delete(festival);
    }

    // ===== Mapper =====
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
