package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.req.FestivalRequest;
import com.ra.base_spring_boot.dto.resp.FestivalResponse;
import com.ra.base_spring_boot.model.entity.content.Festival;
import org.springframework.data.domain.Page;

public interface IFestivalService {

    Page<FestivalResponse> getAllFestivals(
            String search,
            int page,
            int size,
            String sortBy,
            String direction
    );

    FestivalResponse createFestival(FestivalRequest festivalRequest );

    FestivalResponse updateFestival(Long id, FestivalRequest festivalRequest);

    void deleteFestival(Long id);
}
