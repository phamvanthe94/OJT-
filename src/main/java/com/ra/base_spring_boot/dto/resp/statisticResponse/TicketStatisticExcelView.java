package com.ra.base_spring_boot.dto.resp.statisticResponse;

import com.ra.base_spring_boot.model.constants.SeatType;
import lombok.*;

public interface TicketStatisticExcelView {

    String getMovieTitle();

    SeatType getSeatType();

    Double getPrice();

    Long getQuantity();

    Double getTotalAmount();
}

