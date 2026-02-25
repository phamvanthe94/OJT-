package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.req.NewRequest;
import com.ra.base_spring_boot.dto.resp.NewResponse;
import org.springframework.data.domain.Page;

public interface INewService {

    Page<NewResponse> getAllNews(
            String title,
            String content,
            Long festivalId,
            int page,
            int size,
            String sortBy,
            String direction
    );

    NewResponse createNews(NewRequest request);

    NewResponse updateNews(Long id, NewRequest request);

    void deleteNews(Long id);
}
