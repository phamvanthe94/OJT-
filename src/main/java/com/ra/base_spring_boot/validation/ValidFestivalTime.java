package com.ra.base_spring_boot.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FestivalTimeValidator.class)
public @interface ValidFestivalTime {
    String message() default "Thời gian kết thúc không được nhỏ hơn thời gian bắt đầu";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}