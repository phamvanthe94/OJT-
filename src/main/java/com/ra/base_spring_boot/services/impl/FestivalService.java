package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.FestivalDTO;
import com.ra.base_spring_boot.model.entity.content.Festival;
import com.ra.base_spring_boot.repository.FestivalRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class FestivalService {
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private FestivalRepository festivalRepository;

    public ResponseEntity<ResponseWrapper<Page<Festival>>> getAllAndSearchFestival(String search, Pageable pageable) {
        Page<Festival> festivalPage = festivalRepository.findByTitle(search, pageable);
        ResponseWrapper<Page<Festival>> responseWrapper = ResponseWrapper.<Page<Festival>>builder()
                .data(festivalPage)
                .code(200)
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(responseWrapper);
    }

    public ResponseEntity<ResponseWrapper<?>> createFestival(FestivalDTO festivalDTO, BindingResult bindingResult) {
        if (festivalDTO.getImage() == null || festivalDTO.getImage().isEmpty()) {
            bindingResult.rejectValue("image", "400", "Image cannot be null or empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Festival festival = convertFestivalDTOToFestival(festivalDTO);
            String urlImage = cloudinaryService.upload(festivalDTO.getImage());
            festival.setImage(urlImage);
            Festival newFestival = festivalRepository.save(festival);
            ResponseWrapper<Festival> responseWrapper = ResponseWrapper
                    .<Festival>builder()
                    .data(newFestival)
                    .code(201)
                    .status(HttpStatus.CREATED)
                    .build();
            return new ResponseEntity<>(responseWrapper, HttpStatus.CREATED);
        }

    }

    public ResponseEntity<ResponseWrapper<?>> updateFestival(Long id, FestivalDTO festivalDTO) {
        Festival oldFestival = festivalRepository.findById(id).orElseThrow(() -> new RuntimeException("Festival not found with id: " + id));
        if (festivalDTO != null) {
            Festival festival = convertFestivalDTOToFestival(festivalDTO);
            festival.setId(oldFestival.getId());
            if (festivalDTO.getImage() != null && !festivalDTO.getImage().isEmpty()) {
                String urlImage = cloudinaryService.upload(festivalDTO.getImage());
                festival.setImage(urlImage);
            } else {
                festival.setImage(oldFestival.getImage());
            }
            Festival updatedFestival = festivalRepository.save(festival);
            ResponseWrapper<Festival> responseWrapper = ResponseWrapper
                    .<Festival>builder()
                    .data(updatedFestival)
                    .code(200)
                    .status(HttpStatus.OK)
                    .build();
            return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
        }
        return null;
    }

    public ResponseEntity<ResponseWrapper<String>> deleteFestival(Long id) {
        Festival festival = festivalRepository.findById(id).orElseThrow(() -> new RuntimeException("Festival not found with id: " + id));
        festivalRepository.delete(festival);
        ResponseWrapper<String> responseWrapper = ResponseWrapper
                .<String>builder()
                .data("Delete festival successfully")
                .code(200)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    public Festival convertFestivalDTOToFestival(FestivalDTO festivalDTO) {
        return Festival.builder()
                .title(festivalDTO.getTitle())
                .image(null)
                .startTime(festivalDTO.getStartTime())
                .endTime(festivalDTO.getEndTime())
                .build();
    }
}
