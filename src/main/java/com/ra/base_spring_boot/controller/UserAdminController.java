package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.ChangeUserStatusRequest;
import com.ra.base_spring_boot.services.authsv.IUserAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final IUserAdminService userAdminService;

    /**
     * GET /api/v1/admin/users?keyword=...&page=0&size=10&sortBy=id&direction=desc
     */
    @GetMapping
    public ResponseEntity<?> handleGetAllUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(userAdminService.getAllUsers(keyword, page, size, sortBy, direction))
                        .build()
        );
    }

    /**
     * PATCH /api/v1/admin/users/{id}/status
     * Body: { "status": true } or { "status": false }
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> handleChangeUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserStatusRequest request
    ) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(userAdminService.changeUserStatus(id, request))
                        .build()
        );
    }
}