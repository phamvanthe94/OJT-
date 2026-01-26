package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.NewDTO;
import com.ra.base_spring_boot.model.entity.content.Festival;
import com.ra.base_spring_boot.model.entity.content.News;
import com.ra.base_spring_boot.repository.FestivalRepository;
import com.ra.base_spring_boot.repository.NewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class NewService {
    @Autowired
    private NewRepository newRepository;
    @Autowired
    private FestivalRepository festivalRepository;

    public ResponseEntity<ResponseWrapper<Page<News>>> getAllAndSearchNew(String title,
                                                                           String content,
                                                                           Long festivalId,
                                                                           Pageable pageable) {
        Page<News> newsPage = newRepository.search(title, content, festivalId, pageable);
        ResponseWrapper<Page<News>> responseWrapper = ResponseWrapper.<Page<News>>builder()
                .data(newsPage)
                .code(200)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }
    public ResponseEntity<ResponseWrapper<?>> createNew(NewDTO newDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            ResponseWrapper<String> responseWrapper = ResponseWrapper.<String>builder()
                    .data(bindingResult.getAllErrors().toString())
                    .code(400)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
            return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
        } else {
            News news = convertNewDTOToNews(newDTO);
            News newNews = newRepository.save(news);
            ResponseWrapper<News> responseWrapper = ResponseWrapper
                    .<News>builder()
                    .data(newNews)
                    .code(201)
                    .status(HttpStatus.CREATED)
                    .build();
            return new ResponseEntity<>(responseWrapper, HttpStatus.CREATED);
        }
    }
    public ResponseEntity<ResponseWrapper<?>> updateNew(Long id, NewDTO newDTO){
        News oldNews = newRepository.findById(id).orElseThrow(() -> new RuntimeException("News not found with id: " + id));
        if (newDTO != null) {
            News news = convertNewDTOToNews(newDTO);
            news.setId(oldNews.getId());
            News updatedNews = newRepository.save(news);
            ResponseWrapper<News> responseWrapper = ResponseWrapper
                    .<News>builder()
                    .data(updatedNews)
                    .code(200)
                    .status(HttpStatus.OK)
                    .build();
            return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
        } else {
            ResponseWrapper<String> responseWrapper = ResponseWrapper
                    .<String>builder()
                    .data("No data to update")
                    .code(400)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
            return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<ResponseWrapper<String>> deleteNew(Long id){
        News news = newRepository.findById(id).orElseThrow(() -> new RuntimeException("News not found with id: " + id));
        newRepository.delete(news);
        ResponseWrapper<String> responseWrapper = ResponseWrapper
                .<String>builder()
                .data("Delete News successfully")
                .code(200)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    public News convertNewDTOToNews(NewDTO newDTO){
        Festival festival = festivalRepository.findById(newDTO.getFestivalId())
                .orElseThrow(() -> new RuntimeException("Festival not found with id: " + newDTO.getFestivalId()));
        return News.builder()
                .title(newDTO.getTitle())
                .content(newDTO.getContent())
                .festival(festival)
                .createdAt(newDTO.getCreatedAt().atStartOfDay())
                .updatedAt(newDTO.getUpdatedAt().atStartOfDay())
                .build();
    }
}
