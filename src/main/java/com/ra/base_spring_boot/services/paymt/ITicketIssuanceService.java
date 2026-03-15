package com.ra.base_spring_boot.services.paymt;

import com.ra.base_spring_boot.model.entity.booking.Payment;

public interface ITicketIssuanceService {
    void issueTicketAfterPaymentCompleted(Payment payment);
}
