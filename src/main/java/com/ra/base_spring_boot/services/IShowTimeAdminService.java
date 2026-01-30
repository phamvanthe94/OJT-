package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.req.ShowTimeRequest;
import com.ra.base_spring_boot.dto.resp.ShowTimeDetailResponse;
import com.ra.base_spring_boot.dto.resp.ShowTimeListResponse;
import org.springframework.data.domain.Page;

public interface IShowTimeAdminService {
    Page<ShowTimeListResponse> getAllShowTimes(String keyword, int page, int size, String sortBy, String direction);

    ShowTimeDetailResponse getDetailShowTime(Long showTimeId);

    ShowTimeDetailResponse createShowTime(ShowTimeRequest showTimeRequest);

    ShowTimeDetailResponse updateShowTime(Long showTimeId, ShowTimeRequest showTimeRequest);

    void deleteShowTime(Long showTimeId);

}
