package org.virtualpowerplant.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record BatteryResponseDto(
        UUID id,
        String name,
        Integer postcode,
        Double wattCapacity
) {
}
