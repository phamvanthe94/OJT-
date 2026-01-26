package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.NewDTO;
import com.ra.base_spring_boot.model.entity.content.News;
import com.ra.base_spring_boot.services.NewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
public class NewController {
    @Autowired
    private NewService newService;
    @GetMapping
    public ResponseEntity<ResponseWrapper<Page<News>>> getAllNew(@RequestParam(name="title", required = false) String title,
                                                                 @RequestParam(name="content", required = false) String content,
                                                                 @RequestParam(name="festivalId", required = false) Long festivalId,
                                                                 @RequestParam(name="page", defaultValue = "0") int page,
                                                                 @RequestParam(name="size", defaultValue = "5") int size){
        return newService.getAllAndSearchNew(title, content, festivalId, PageRequest.of(page, size));
    }
    @PostMapping("/add")
    public ResponseEntity<ResponseWrapper<?>> addNew(@RequestBody @Valid NewDTO newDTO, BindingResult bindingResult){
        ResponseEntity<ResponseWrapper<?>> responseEntity = newService.createNew(newDTO, bindingResult);
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(
                    ResponseWrapper
                            .<Object>builder()
                            .data(bindingResult.getAllErrors())
                            .code(400)
                            .status(org.springframework.http.HttpStatus.BAD_REQUEST)
                            .build()
            );
        }
        return responseEntity;
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseWrapper<?>> updateNew(@PathVariable Long id, @RequestBody @Valid NewDTO newDTO) {
        return newService.updateNew(id, newDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteNew(@PathVariable Long id) {
        return newService.deleteNew(id);
    }
}
