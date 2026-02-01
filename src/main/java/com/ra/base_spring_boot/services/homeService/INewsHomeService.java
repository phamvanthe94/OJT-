package com.ra.base_spring_boot.services.homeService;

import com.ra.base_spring_boot.dto.resp.homeresp.NewsDetailResponse;
import com.ra.base_spring_boot.dto.resp.homeresp.NewsListResponse;
import org.springframework.data.domain.Page;

public interface INewsHomeService {

    Page<NewsListResponse> getHomeNews(int page, int size);

    NewsDetailResponse getHomeNewDetail(Long id);

}
