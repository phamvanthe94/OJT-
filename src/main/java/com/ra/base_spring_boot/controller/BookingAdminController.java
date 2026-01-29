package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.resp.ChangePaymentStatusDTO;
import com.ra.base_spring_boot.model.entity.booking.Booking;
import com.ra.base_spring_boot.services.BookingAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/bookings")
public class BookingAdminController {

    private final BookingAdminService bookingAdminService;

    // ✅ 1) LIST + SEARCH + PAGING
    @GetMapping
    public ResponseEntity<ResponseWrapper<Page<Booking>>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort
    ) {
        String[] s = sort.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(s[1]), s[0]));
        return bookingAdminService.getAll(keyword, pageable);
    }

    // ✅ 2) DETAIL
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> detail(@PathVariable Long id) {
        return bookingAdminService.getDetail(id);
    }

    // ✅ 3) CHANGE STATUS
    @PutMapping("/{id}/status")
    public ResponseEntity<ResponseWrapper<Object>> changeStatus(
            @PathVariable Long id,
            @RequestBody ChangePaymentStatusDTO dto
    ) {
        return bookingAdminService.changePaymentStatus(id, dto);
    }
}