package org.virtualpowerplant.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BatteryRequestDto(
        @NotBlank String name,
        @NotNull Integer postcode,
        @NotNull Double wattCapacity
) {
}
