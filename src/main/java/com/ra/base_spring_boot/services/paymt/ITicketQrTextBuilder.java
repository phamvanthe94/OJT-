package com.ra.base_spring_boot.services.paymt;

import com.ra.base_spring_boot.dto.resp.TicketQrData;

import java.util.List;

public interface ITicketQrTextBuilder {
    String buildQrText(List<TicketQrData> list);
}
