package com.ra.base_spring_boot.services.paymt.impl;

import com.ra.base_spring_boot.dto.resp.TicketQrData;
import com.ra.base_spring_boot.services.paymt.ITicketQrTextBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketQrTextBuilderImpl implements ITicketQrTextBuilder {

    private static final DateTimeFormatter JP_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public String buildQrText(List<TicketQrData> list) {
        if (list == null || list.isEmpty()) return "";

        TicketQrData first = list.get(0);

        String seats = list.stream()
                .map(TicketQrData::getSeatNumber)
                .filter(s -> s != null && !s.isBlank())
                .map(String::trim)
                .distinct()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.joining(", "));

        String startTimeText = formatStartTime(first.getStartTime());

        return """
                MOVIE TICKET
                ----------------------------
                Booking ID: %s
                Customer: %s
                Movie: %s
                Room: %s
                Showtime: %s
                Seats: %s
                ----------------------------
                """.formatted(
                safe(first.getBookingId()),
                safe(first.getFullName()),
                safe(first.getTitle()),
                safe(first.getScreenName()),
                safe(startTimeText),
                safe(seats)
        );
    }

    private String formatStartTime(LocalDateTime t) {
        if (t == null) return "";
        return t.format(JP_TIME_FMT);
    }

    private String safe(Object o) {
        return o == null ? "" : String.valueOf(o).trim();
    }
}
