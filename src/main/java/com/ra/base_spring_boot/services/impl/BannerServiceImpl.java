package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.BannerDTO;
import com.ra.base_spring_boot.exception.HttpBadRequest;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.constants.BannerType;
import com.ra.base_spring_boot.model.entity.content.Banner;
import com.ra.base_spring_boot.repository.IBannerRepository;
import com.ra.base_spring_boot.services.IBannerService;
import com.ra.base_spring_boot.services.more.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements IBannerService {

    private final IBannerRepository bannerRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public Page<Banner> getAllBanners(String search, int page, int size, String sortBy, String direction) {
        String sortField = sortBy == null || sortBy.isBlank() ? "id" : sortBy;
        Sort sort = "asc".equalsIgnoreCase(direction)
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), sort);

        if (search == null || search.isBlank()) {
            return bannerRepository.findAll(pageable);
        }

        return bannerRepository.findByPositionContainingIgnoreCaseOrUrlContainingIgnoreCase(
                search.trim(),
                search.trim(),
                pageable
        );
    }

    @Override
    public Banner createBanner(BannerDTO bannerDTO) {
        if (bannerDTO.getUrl() == null || bannerDTO.getUrl().isEmpty()) {
            throw new HttpBadRequest("URL cannot be null or empty");
        }

        BannerType type = null;
        if (bannerDTO.getType() != null && !bannerDTO.getType().isBlank()) {
            type = BannerType.valueOf(bannerDTO.getType());
        }

        Banner banner = Banner.builder()
                .url(cloudinaryService.upload(bannerDTO.getUrl()))
                .type(type)
                .position(bannerDTO.getPosition())
                .build();

        return bannerRepository.save(banner);
    }

    @Override
    public void deleteBanner(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Banner not found"));
        bannerRepository.delete(banner);
    }
}
