package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.BannerDTO;
import com.ra.base_spring_boot.model.constants.BannerType;
import com.ra.base_spring_boot.model.entity.content.Banner;
import com.ra.base_spring_boot.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class BannerService {
    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    public ResponseEntity<ResponseWrapper<Page<Banner>>> getAllAndSearch(String search, Pageable pageable) {
        Page<Banner> bannerPage = bannerRepository.findByPosition(search, pageable);
        ResponseWrapper<Page<Banner>> responseWrapper = ResponseWrapper.<Page<Banner>>builder()
                .data(bannerPage)
                .code(200)
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(responseWrapper);
    }

    public ResponseEntity<ResponseWrapper<?>> createBanner(BannerDTO bannerDTO, BindingResult bindingResult) {
        if (bannerDTO.getUrl() == null || bannerDTO.getUrl().isEmpty()) {
            bindingResult.rejectValue("url", "400", "URL cannot be null or empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Banner banner = convertBannerDTOToBanner(bannerDTO);
            String urlBanner = cloudinaryService.upload(bannerDTO.getUrl());
            banner.setUrl(urlBanner);
            Banner newBanner = bannerRepository.save(banner);
            ResponseWrapper<Banner> responseWrapper = ResponseWrapper
                    .<Banner>builder()
                    .data(newBanner)
                    .code(201)
                    .status(HttpStatus.CREATED)
                    .build();
            return new ResponseEntity<>(responseWrapper, HttpStatus.CREATED);
        }
    }

    public ResponseEntity<ResponseWrapper<String>> deleteBanner(Long id) {
        Banner banner = bannerRepository.findById(id).orElseThrow(() -> new RuntimeException("Banner not found with id: " + id));
        bannerRepository.delete(banner);
        ResponseWrapper<String> responseWrapper = ResponseWrapper
                .<String>builder()
                .data("Delete banner successfully")
                .code(200)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    public Banner convertBannerDTOToBanner(BannerDTO bannerDTO) {
        BannerType type = null;
        if (bannerDTO.getType() != null && !bannerDTO.getType().isEmpty()) {
            try {
                type = BannerType.valueOf(bannerDTO.getType());
            } catch (IllegalArgumentException e) {
            }
        }
        return Banner.builder()
                .url(null)
                .type(type)
                .position(bannerDTO.getPosition())
                .build();


    }
}
