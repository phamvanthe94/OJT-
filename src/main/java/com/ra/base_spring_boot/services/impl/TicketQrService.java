package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.resp.TicketQrData;
import com.ra.base_spring_boot.repository.TicketQrRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketQrService {
    private final TicketQrRepository ticketQrRepository;

    public List<TicketQrData> getQrData(Long bookingId) {
        List<TicketQrData> qrDataList = ticketQrRepository.getQrDataByBookingId(bookingId);
        if (qrDataList.isEmpty()) {
            throw new RuntimeException("Booking not paid or not found");
        }
        return qrDataList;
    }
}
