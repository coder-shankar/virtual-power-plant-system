package org.virtualpowerplant.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.virtualpowerplant.model.BatteryRequestDto;
import org.virtualpowerplant.model.BatteryResponseDto;
import org.virtualpowerplant.model.BatterySearchCriteria;
import org.virtualpowerplant.service.BatteryService;
import org.virtualpowerplant.model.BatteryStatisticsDto;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/batteries",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class BatteryController {

    private final BatteryService batteryService;

    @PostMapping()
    public ResponseEntity<List<BatteryResponseDto>> registerBatteries(@Valid @RequestBody List<BatteryRequestDto> request) {
        return ResponseEntity.ok(batteryService.registerBatteries(request));
    }

    @GetMapping(value = "/stats")
    public ResponseEntity<BatteryStatisticsDto> getBatteriesInRange(
            @Valid @ModelAttribute BatterySearchCriteria batterySearchCriteria
    ) {
        BatteryStatisticsDto stats = batteryService.getBatteryStatistics(batterySearchCriteria);
        return ResponseEntity.ok(stats);
    }
}
