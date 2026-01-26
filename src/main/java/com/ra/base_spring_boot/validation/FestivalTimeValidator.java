package com.ra.base_spring_boot.validation;

import com.ra.base_spring_boot.dto.req.FestivalDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FestivalTimeValidator
        implements ConstraintValidator<ValidFestivalTime, FestivalDTO> {

    @Override
    public boolean isValid(FestivalDTO dto, ConstraintValidatorContext context) {
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            return true; // @NotNull xử lý riêng
        }
        return !dto.getEndTime().isBefore(dto.getStartTime());
    }
}
