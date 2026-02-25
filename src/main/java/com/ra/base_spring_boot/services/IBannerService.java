package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.req.BannerDTO;
import com.ra.base_spring_boot.model.entity.content.Banner;
import org.springframework.data.domain.Page;

public interface IBannerService {

    Page<Banner> getAllBanners(
            String search,
            int page,
            int size,
            String sortBy,
            String direction
    );

    Banner createBanner(BannerDTO bannerDTO);

    void deleteBanner(Long id);
}
