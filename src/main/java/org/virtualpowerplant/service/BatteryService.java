package org.virtualpowerplant.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.virtualpowerplant.entity.Battery;
import org.virtualpowerplant.mapper.BatteryMapper;
import org.virtualpowerplant.model.BatteryRequestDto;
import org.virtualpowerplant.model.BatteryResponseDto;
import org.virtualpowerplant.model.BatterySearchCriteria;
import org.virtualpowerplant.model.BatteryStatisticsDto;
import org.virtualpowerplant.repository.BatteryRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BatteryService {

    static final Logger logger = LoggerFactory.getLogger(BatteryService.class);

    private final BatteryRepository batteryRepository;
    private final BatteryMapper batteryMapper;


    @Transactional
    public List<BatteryResponseDto> registerBatteries(List<BatteryRequestDto> batteryRequests) {
        if (batteryRequests.isEmpty()) {
            logger.debug("No batteries to register");
            return List.of();
        }

        logger.info("Registering batteries: {}", batteryRequests);
        var batteries = batteryRequests.parallelStream()
                                       .map(batteryMapper::toEntity)
                                       .toList();

        return batteryRepository.saveAll(batteries)
                                .parallelStream()
                                .map(batteryMapper::toResponse)
                                .toList();
    }

    public BatteryStatisticsDto getBatteryStatistics(BatterySearchCriteria criteria) {
        logger.info("Getting battery statistics for criteria: {}", criteria);

        var batteries = batteryRepository.filter(criteria);
        var total = batteries.stream()
                             .mapToDouble(Battery::getWattCapacity)
                             .sum();
        var average = batteries.isEmpty() ? 0 : total / batteries.size();

        return BatteryStatisticsDto.builder()
                                   .batteryNames(batteries.stream().map(Battery::getName).toList())
                                   .totalWattCapacity(total)
                                   .averageWattCapacity(average)
                                   .build();
    }
}