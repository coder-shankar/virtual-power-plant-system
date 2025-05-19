package org.virtualpowerplant.mapper;

import org.junit.jupiter.api.Test;
import org.virtualpowerplant.entity.Battery;
import org.virtualpowerplant.model.BatteryRequestDto;
import org.virtualpowerplant.model.BatteryResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

class BatteryMapperTest {

    private final BatteryMapper mapper = BatteryMapper.INSTANCE;

    @Test
    void toEntity_shouldMapAllFields() {
        // Arrange
        var requestDto = BatteryRequestDto.builder()
                                          .name("Test Battery")
                                          .postcode(1234)
                                          .wattCapacity(100.0)
                                          .build();

        // Act
        Battery result = mapper.toEntity(requestDto);

        // Assert
        assertThat(result)
                .isNotNull()
                .satisfies(battery -> {
                    assertThat(battery.getName()).isEqualTo("Test Battery");
                    assertThat(battery.getPostcode()).isEqualTo(1234);
                    assertThat(battery.getWattCapacity()).isEqualTo(100.0);
                });
    }

    @Test
    void toResponse_shouldMapAllFields() {
        // Arrange
        var battery = Battery.builder()
                             .name("Test Battery")
                             .postcode(1234)
                             .wattCapacity(100.0)
                             .build();

        // Act
        BatteryResponseDto result = mapper.toResponse(battery);

        // Assert
        assertThat(result)
                .isNotNull()
                .satisfies(response -> {
                    assertThat(response.name()).isEqualTo("Test Battery");
                    assertThat(response.postcode()).isEqualTo(1234);
                    assertThat(response.wattCapacity()).isEqualTo(100.0);
                });
    }

    @Test
    void toEntity_shouldHandleNullValues() {
        // Arrange
        var requestDto = BatteryRequestDto.builder()
                                          .name("Test Battery")
                                          .build();

        // Act
        Battery result = mapper.toEntity(requestDto);

        // Assert
        assertThat(result)
                .isNotNull()
                .satisfies(battery -> {
                    assertThat(battery.getName()).isEqualTo("Test Battery");
                    assertThat(battery.getPostcode()).isNull();
                    assertThat(battery.getWattCapacity()).isNull();
                });
    }

    @Test
    void toResponse_shouldHandleNullValues() {
        // Arrange
        var battery = Battery.builder()
                             .name("Test Battery")
                             .build();

        // Act
        BatteryResponseDto result = mapper.toResponse(battery);

        // Assert
        assertThat(result)
                .isNotNull()
                .satisfies(response -> {
                    assertThat(response.name()).isEqualTo("Test Battery");
                    assertThat(response.postcode()).isNull();
                    assertThat(response.wattCapacity()).isNull();
                });
    }
}