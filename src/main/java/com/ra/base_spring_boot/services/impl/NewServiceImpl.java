package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.NewRequest;
import com.ra.base_spring_boot.dto.resp.NewResponse;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.entity.content.Festival;
import com.ra.base_spring_boot.model.entity.content.News;
import com.ra.base_spring_boot.repository.IFestivalRepository;
import com.ra.base_spring_boot.repository.INewRepository;
import com.ra.base_spring_boot.services.INewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewServiceImpl implements INewService {

    private final INewRepository newRepository;
    private final IFestivalRepository festivalRepository;

    @Override
    public Page<NewResponse> getAllNews(
            String title,
            String content,
            Long festivalId,
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

        Page<News> newsPage = newRepository.search(
                title,
                content,
                festivalId,
                pageable
        );

        return newsPage.map(this::toResponse);
    }

    @Override
    public NewResponse createNews(NewRequest request) {

        Festival festival = festivalRepository.findById(request.getFestivalId())
                .orElseThrow(() -> new HttpNotFound(
                        "Festival not found with id: " + request.getFestivalId()
                ));

        News news = News.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .festival(festival)
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();

        return toResponse(newRepository.save(news));
    }

    @Override
    public NewResponse updateNews(Long id, NewRequest request) {

        News oldNews = newRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("News not found with id: " + id));

        Festival festival = festivalRepository.findById(request.getFestivalId())
                .orElseThrow(() -> new HttpNotFound(
                        "Festival not found with id: " + request.getFestivalId()
                ));

        oldNews.setTitle(request.getTitle());
        oldNews.setContent(request.getContent());
        oldNews.setFestival(festival);
        oldNews.setUpdatedAt(request.getUpdatedAt());

        return toResponse(newRepository.save(oldNews));
    }

    @Override
    public void deleteNews(Long id) {
        News news = newRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("News not found with id: " + id));
        newRepository.delete(news);
    }

    private NewResponse toResponse(News news) {
        Festival festival = news.getFestival();
        return NewResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .festivalTitle(festival == null ? null : festival.getTitle())
                .createdAt(news.getCreatedAt())
                .build();
    }
}
