package com.ra.base_spring_boot.dto.req;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TicketPriceRequest {
    @NotBlank(message = "Type seat cannot be blank")
    private String typeSeat;
    @NotBlank(message = " Type movie cannot be blank")
    private String typeMovie;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private Double price;

    @NotNull(message = " Day type cannot be null")
    private Boolean dayType;
    @NotNull(message = " Start time cannot be null")
    private LocalTime startTime;
    @NotNull(message = " End time cannot be null")
    private LocalTime endTime;



}
