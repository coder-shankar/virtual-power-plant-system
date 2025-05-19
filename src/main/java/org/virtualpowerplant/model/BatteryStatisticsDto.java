package org.virtualpowerplant.model;

import lombok.Builder;

import java.util.List;

@Builder
public record BatteryStatisticsDto(
        List<String> batteryNames,
        double totalWattCapacity,
        double averageWattCapacity
) {
}