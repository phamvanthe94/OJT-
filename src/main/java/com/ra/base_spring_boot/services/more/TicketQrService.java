package com.ra.base_spring_boot.services.more;

import com.ra.base_spring_boot.dto.resp.TicketQrData;
import com.ra.base_spring_boot.repository.ITicketQrRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketQrService {

    private final ITicketQrRepository ticketQrRepository;

    public List<TicketQrData> getQrData(Long bookingId) {
        return ticketQrRepository.getQrDataByBookingId(bookingId);
    }
}
