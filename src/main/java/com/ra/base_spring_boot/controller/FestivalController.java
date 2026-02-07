package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.FestivalDTO;
import com.ra.base_spring_boot.model.entity.content.Festival;
import com.ra.base_spring_boot.services.impl.FestivalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/festivals")
public class FestivalController {
    @Autowired
    private FestivalService festivalService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<Page<Festival>>> getAllFestival(@RequestParam(name = "keyword", required = false) String keyword,
                                                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                                                          @RequestParam(name = "size", defaultValue = "5") int size) {
        return festivalService.getAllAndSearchFestival(keyword, PageRequest.of(page, size));
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseWrapper<?>> addFestival(@Valid @ModelAttribute FestivalDTO festivalDTO, BindingResult bindingResult) {
        ResponseEntity<ResponseWrapper<?>> responseEntity = festivalService.createFestival(festivalDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    ResponseWrapper
                            .<Object>builder()
                            .data(bindingResult.getAllErrors())
                            .code(400)
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        }
        return responseEntity;
    }

    @PutMapping(value = "/edit/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseWrapper<?>> updateFestival(@Valid @ModelAttribute FestivalDTO festivalDTO, @PathVariable Long id) {
        return festivalService.updateFestival(id, festivalDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteFestival(@PathVariable Long id) {
        return festivalService.deleteFestival(id);
    }
}
