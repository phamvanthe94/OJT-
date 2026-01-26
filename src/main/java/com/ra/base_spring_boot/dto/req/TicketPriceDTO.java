package com.ra.base_spring_boot.dto.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TicketPriceDTO {
    @NotBlank(message = "Type seat cannot be blank")
    private String typeSeat;
    @NotBlank(message = " Type movie cannot be blank")
    private String typeMovie;
    @NotNull
    @Min(value = 0, message = " Price must be greater than or equal to 0")
    private Double price;

    @NotNull(message = " Day type cannot be null")
    private Boolean dayType; // false: T2-5, true: T6-7-CN-Lễ
    @NotNull(message = " Start time cannot be null")
    private LocalTime startTime;
    @NotNull(message = " End time cannot be null")
    private LocalTime endTime;



}
