package com.ra.base_spring_boot.services.statistic.impl;

import com.ra.base_spring_boot.dto.resp.statisticResponse.NewAndFestivalResponse;
import com.ra.base_spring_boot.dto.resp.statisticResponse.NewAndFestivalStatisticResponse;
import com.ra.base_spring_boot.repository.IFestivalRepository;
import com.ra.base_spring_boot.repository.INewRepository;
import com.ra.base_spring_boot.services.statistic.INewAndFestivalStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewAndFestivalStatisticServiceImpl
        implements INewAndFestivalStatisticService {

    private final INewRepository newRepository;
    private final IFestivalRepository festivalRepository;

    @Override
    public NewAndFestivalStatisticResponse statisticNewsAndFestival() {

        List<NewAndFestivalResponse> news = newRepository.findAllNewsTitle()
                .stream()
                .map(n -> NewAndFestivalResponse.builder()
                        .id(n.getId())
                        .title(n.getTitle())
                        .build())
                .toList();

        List<NewAndFestivalResponse> festivals = festivalRepository.findAllFestivalTitle()
                .stream()
                .map(f -> NewAndFestivalResponse.builder()
                        .id(f.getId())
                        .title(f.getTitle())
                        .build())
                .toList();

        return NewAndFestivalStatisticResponse.builder()
                .totalNews((long) news.size())
                .news(news)
                .totalFestivals((long) festivals.size())
                .festivals(festivals)
                .build();
    }
}

