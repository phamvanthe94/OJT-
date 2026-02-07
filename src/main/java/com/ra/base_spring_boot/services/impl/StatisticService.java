package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.repository.FestivalRepository;
import com.ra.base_spring_boot.repository.NewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticService {
    @Autowired
    private NewRepository newRepository;
    @Autowired
    private FestivalRepository festivalRepository;

    public ResponseEntity<ResponseWrapper<?>> statisticNewsAndFestivalDetail() {

        Map<String, Object> result = new HashMap<>();

        result.put("totalNews", newRepository.count());
        result.put("news", newRepository.findAllNewsTitle());

        result.put("totalFestivals", festivalRepository.count());
        result.put("festivals", festivalRepository.findAllFestivalTitle());

        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .data(result)
                        .code(200)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

}
