package com.ra.base_spring_boot.validation;

import com.ra.base_spring_boot.dto.req.FestivalRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FestivalTimeValidator
        implements ConstraintValidator<ValidFestivalTime, FestivalRequest> {

    @Override
    public boolean isValid(FestivalRequest dto, ConstraintValidatorContext context) {
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            return true;
        }
        return !dto.getEndTime().isBefore(dto.getStartTime());
    }
}
