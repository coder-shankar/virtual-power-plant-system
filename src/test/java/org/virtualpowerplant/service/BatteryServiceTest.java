package org.virtualpowerplant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.virtualpowerplant.entity.Battery;
import org.virtualpowerplant.mapper.BatteryMapper;
import org.virtualpowerplant.model.BatteryRequestDto;
import org.virtualpowerplant.model.BatteryResponseDto;
import org.virtualpowerplant.model.BatterySearchCriteria;
import org.virtualpowerplant.model.BatteryStatisticsDto;
import org.virtualpowerplant.repository.BatteryRepository;
import org.virtualpowerplant.util.TestDataBuilder;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BatteryServiceTest {

    @Mock
    private BatteryRepository batteryRepository;

    @Mock
    private BatteryMapper batteryMapper;

    @InjectMocks
    private BatteryService batteryService;

    private BatteryRequestDto requestDto;
    private Battery battery;
    private BatteryResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = TestDataBuilder.BatteryRequestBuilder.cannington();
        battery = TestDataBuilder.BatteryBuilder.cannington();
        responseDto = TestDataBuilder.BatteryResponseBuilder.cannington();
    }

    @Test
    void registerBatteries_WithValidRequest_ShouldReturnResponseList() {
        // Arrange
        var requests = List.of(requestDto);
        var entities = List.of(battery);

        when(batteryMapper.toEntity(any(BatteryRequestDto.class))).thenReturn(battery);
        when(batteryRepository.saveAll(anyList())).thenReturn(entities);
        when(batteryMapper.toResponse(any(Battery.class))).thenReturn(responseDto);

        // Act
        List<BatteryResponseDto> result = batteryService.registerBatteries(requests);

        // Assert
        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .first()
                .satisfies(response -> {
                    assertThat(response.name()).isEqualTo("Cannington");
                    assertThat(response.postcode()).isEqualTo(6107);
                    assertThat(response.wattCapacity()).isEqualTo(13500);
                });

        verify(batteryMapper).toEntity(requestDto);
        verify(batteryRepository).saveAll(anyList());
        verify(batteryMapper).toResponse(battery);
    }

    @Test
    void registerBatteries_WithEmptyList_ShouldReturnEmptyList() {
        // Arrange
        List<BatteryRequestDto> emptyList = Collections.emptyList();

        // Act
        List<BatteryResponseDto> result = batteryService.registerBatteries(emptyList);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void getBatteryStatistics_WithValidCriteria_ShouldReturnStatistics() {
        // Arrange
        var criteria = TestDataBuilder.SearchCriteriaBuilder.defaultCriteria();
        var batteries = TestDataBuilder.BatteryBuilder.allBatteries();

        when(batteryRepository.filter(criteria)).thenReturn(batteries);

        // Act
        BatteryStatisticsDto result = batteryService.getBatteryStatistics(criteria);

        // Assert
        assertThat(result)
                .isNotNull()
                .satisfies(stats -> {
                    assertThat(stats.batteryNames())
                            .containsExactly("Cannington", "Midland", "Hay Street");
                    assertThat(stats.totalWattCapacity()).isEqualTo(TestDataBuilder.TOTAL_CAPACITY);
                    assertThat(stats.averageWattCapacity()).isEqualTo(TestDataBuilder.AVERAGE_CAPACITY);
                });

        verify(batteryRepository).filter(criteria);
    }

    @Test
    void getBatteryStatistics_WithNoBatteriesFound_ShouldReturnEmptyStatistics() {
        // Arrange
        var criteria = BatterySearchCriteria.builder()
                                            .startPostcode(1000)
                                            .endPostcode(2000)
                                            .build();

        when(batteryRepository.filter(criteria)).thenReturn(Collections.emptyList());

        // Act
        BatteryStatisticsDto result = batteryService.getBatteryStatistics(criteria);

        // Assert
        assertThat(result)
                .isNotNull()
                .satisfies(stats -> {
                    assertThat(stats.batteryNames()).isEmpty();
                    assertThat(stats.totalWattCapacity()).isZero();
                    assertThat(stats.averageWattCapacity()).isZero();
                });

        verify(batteryRepository).filter(criteria);
    }


    @Test
    void getBatteryStatistics_WithSingleBattery_ShouldCalculateCorrectStats() {
        // Arrange
        var criteria = BatterySearchCriteria.builder()
                                            .startPostcode(1000)
                                            .endPostcode(2000)
                                            .build();

        var batteryCannigton = Battery.builder()
                             .name("Cannington")
                             .wattCapacity(150.0)
                             .build();

        when(batteryRepository.filter(criteria)).thenReturn(List.of(batteryCannigton));

        // Act
        BatteryStatisticsDto result = batteryService.getBatteryStatistics(criteria);

        // Assert
        assertThat(result)
                .satisfies(stats -> {
                    assertThat(stats.batteryNames()).containsExactly("Cannington");
                    assertThat(stats.totalWattCapacity()).isEqualTo(150.0);
                    assertThat(stats.averageWattCapacity()).isEqualTo(150.0);
                });
    }

    @Test
    void getBatteryStatistics_WithExtremeBatteryCapacities_ShouldCalculateCorrectly() {
        // Arrange
        var criteria = BatterySearchCriteria.builder()
                                            .startPostcode(1000)
                                            .endPostcode(2000)
                                            .build();

        var batteries = List.of(
                Battery.builder().name("Cannington").wattCapacity(0.1).build(),
                Battery.builder().name("Midland").wattCapacity(999999.9).build()
        );

        when(batteryRepository.filter(criteria)).thenReturn(batteries);

        // Act
        BatteryStatisticsDto result = batteryService.getBatteryStatistics(criteria);

        // Assert
        assertThat(result)
                .satisfies(stats -> {
                    assertThat(stats.batteryNames()).containsExactly("Cannington", "Midland");
                    assertThat(stats.totalWattCapacity()).isEqualTo(1000000.0);
                    assertThat(stats.averageWattCapacity()).isEqualTo(500000.0);
                });
    }

}