package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.BannerDTO;
import com.ra.base_spring_boot.model.entity.content.Banner;
import com.ra.base_spring_boot.services.BannerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/banners")
public class BannerController {
    @Autowired
    private BannerService bannerService;

    @GetMapping
public ResponseEntity<ResponseWrapper<Page<Banner>>> getAllBanner(@RequestParam(name="search", required = false) String search,
                                                                  @RequestParam(name="page", defaultValue = "0") int page,
                                                                  @RequestParam(name="size", defaultValue = "5") int size){
        return bannerService.getAllAndSearch(search, PageRequest.of(page, size));
    }

    @PostMapping("add")
    public ResponseEntity<ResponseWrapper<?>> addBanner(@Valid @ModelAttribute BannerDTO bannerDTO, BindingResult bindingResult){
        ResponseEntity<ResponseWrapper<?>> responseEntity = bannerService.createBanner(bannerDTO, bindingResult);
        if(bindingResult.hasErrors()) {
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
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteBanner(@PathVariable Long id) {
        return bannerService.deleteBanner(id);

    }
    }
