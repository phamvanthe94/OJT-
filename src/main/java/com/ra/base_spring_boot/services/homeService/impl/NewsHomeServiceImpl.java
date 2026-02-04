package com.ra.base_spring_boot.services.homeService.impl;

import com.ra.base_spring_boot.dto.resp.homeresp.NewsDetailResponse;
import com.ra.base_spring_boot.dto.resp.homeresp.NewsListResponse;
import com.ra.base_spring_boot.model.entity.content.News;
import com.ra.base_spring_boot.repository.homerpo.INewsHomeRepository;
import com.ra.base_spring_boot.services.homeService.INewsHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsHomeServiceImpl implements INewsHomeService {

    private final INewsHomeRepository newsHomeRepository;

    @Override
    public Page<NewsListResponse> getHomeNews(int page, int size) {

        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by("createdAt").descending()

        );

        Page<News> newsPage = newsHomeRepository.findAllByOrderByCreatedAtDesc(pageable);

        return newsPage.map(news -> NewsListResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .excerpt(buildExcerpt(news.getContent()))
                .festivalId(news.getFestival() != null ? news.getFestival().getId() : null)
                .createdAt(news.getCreatedAt())
                .build());
    }

    @Override
    public NewsDetailResponse getHomeNewDetail(Long id) {

        News news = newsHomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tin tức !"));

        return NewsDetailResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .festivalId(news.getFestival() != null ? news.getFestival().getId() : null)
                .createdAt(news.getCreatedAt())
                .build();
    }


    private String buildExcerpt(String content) {
        if (content == null || content.isBlank()) {
            return "";
        }
        String clean = content.replaceAll("\\s+", " ").trim();
        int max = 140;
        return clean.substring(0, max) + "...";


    }
}
