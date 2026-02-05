package com.ra.base_spring_boot.services.homeService;

import com.ra.base_spring_boot.dto.resp.homeresp.FestivalDetailResponse;
import com.ra.base_spring_boot.dto.resp.homeresp.FestivalListResponse;
import org.springframework.data.domain.Page;

public interface IFestivalHomeService {

    Page<FestivalListResponse> getFestivals(int page, int size);

    FestivalDetailResponse getFestivalDetail(Long id);
}
