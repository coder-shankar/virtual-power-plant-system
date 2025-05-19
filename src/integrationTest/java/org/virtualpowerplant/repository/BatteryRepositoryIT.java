package org.virtualpowerplant.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.virtualpowerplant.TestContainersConfiguration;
import org.virtualpowerplant.entity.Battery;
import org.virtualpowerplant.model.BatterySearchCriteria;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@ContextConfiguration(classes = {TestContainersConfiguration.class})
@Sql(scripts = "/sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class BatteryRepositoryIT {

    @Autowired
    private BatteryRepository repository;

    @Test
    @DisplayName("Should successfully save multiple valid batteries")
    void shouldSaveMultipleBatteries() {
        // arrange
        var batteries = List.of(
                Battery.builder()
                       .name("Midland")
                       .postcode(6057)
                       .wattCapacity(50500.0)
                       .build(),
                Battery.builder()
                       .name("Cannington")
                       .postcode(6107)
                       .wattCapacity(13500.0)
                       .build()
        );

        // act
        var savedBatteries = repository.saveAll(batteries);

        // assert
        assertAll("Saved batteries validation",
                () -> assertThat(savedBatteries)
                        .hasSize(2)
                        .allSatisfy(battery -> {
                            assertThat(battery.getId()).isNotNull();
                            assertThat(battery.getCreatedAt()).isNotNull();
                            assertThat(battery.getUpdatedAt()).isNotNull();
                        }),
                () -> {
                    var firstBattery = savedBatteries.getFirst();
                    assertThat(firstBattery.getName()).isEqualTo("Midland");
                    assertThat(firstBattery.getPostcode()).isEqualTo(6057);
                    assertThat(firstBattery.getWattCapacity()).isEqualTo(50500.0);
                },
                () -> {
                    var secondBattery = savedBatteries.get(1);
                    assertThat(secondBattery.getName()).isEqualTo("Cannington");
                    assertThat(secondBattery.getPostcode()).isEqualTo(6107);
                    assertThat(secondBattery.getWattCapacity()).isEqualTo(13500.0);
                }
        );
    }


    @Test
    @DisplayName("Should filter batteries by postcode range only")
    void shouldFilterBatteriesByPostcodeRange() {
        // arrange
        var criteria = BatterySearchCriteria.builder()
                                            .startPostcode(6000)
                                            .endPostcode(6100)
                                            .build();

        // act
        List<Battery> filteredBatteries = repository.filter(criteria);

        // assert
        assertAll("Filtered batteries by postcode validation",
                () -> assertThat(filteredBatteries).isNotEmpty(),
                () -> assertThat(filteredBatteries)
                        .allMatch(battery ->
                                battery.getPostcode() >= criteria.startPostcode()
                                        && battery.getPostcode() <= criteria.endPostcode()),
                () -> assertThat(filteredBatteries)
                        .isSortedAccordingTo((b1, b2) ->
                                b1.getName().compareToIgnoreCase(b2.getName()))
        );
    }

    @Test
    @DisplayName("Should filter batteries by capacity range")
    void shouldFilterBatteriesByCapacityRange() {
        // arrange
        var criteria = BatterySearchCriteria.builder()
                                            .minCapacity(10000.0)
                                            .maxCapacity(50000.0)
                                            .build();

        // act
        List<Battery> filteredBatteries = repository.filter(criteria);

        // assert
        assertAll("Filtered batteries by capacity validation",
                () -> assertThat(filteredBatteries).isNotEmpty(),
                () -> assertThat(filteredBatteries)
                        .allMatch(battery ->
                                battery.getWattCapacity() >= criteria.minCapacity()
                                        && battery.getWattCapacity() <= criteria.maxCapacity()),
                () -> assertThat(filteredBatteries)
                        .isSortedAccordingTo((b1, b2) ->
                                b1.getName().compareToIgnoreCase(b2.getName()))
        );
    }

    @Test
    @DisplayName("Should filter batteries by both postcode and capacity ranges")
    void shouldFilterBatteriesByPostcodeAndCapacityRanges() {
        // arrange
        var criteria = BatterySearchCriteria.builder()
                                            .startPostcode(6000)
                                            .endPostcode(6100)
                                            .minCapacity(10000.0)
                                            .maxCapacity(50000.0)
                                            .build();

        // act
        List<Battery> filteredBatteries = repository.filter(criteria);

        // assert
        assertAll("Filtered batteries by postcode and capacity validation",
                () -> assertThat(filteredBatteries).isNotEmpty(),
                () -> assertThat(filteredBatteries)
                        .allMatch(battery ->
                                battery.getPostcode() >= criteria.startPostcode()
                                        && battery.getPostcode() <= criteria.endPostcode()
                                        && battery.getWattCapacity() >= criteria.minCapacity()
                                        && battery.getWattCapacity() <= criteria.maxCapacity()),
                () -> assertThat(filteredBatteries)
                        .isSortedAccordingTo((b1, b2) ->
                                b1.getName().compareToIgnoreCase(b2.getName()))
        );
    }

    @Test
    @DisplayName("Should return empty list when no batteries match criteria")
    void shouldReturnEmptyListWhenNoBatteriesMatchCriteria() {
        // arrange
        var criteria = BatterySearchCriteria.builder()
                                            .startPostcode(1000)
                                            .endPostcode(2000)
                                            .build();

        // act
        List<Battery> filteredBatteries = repository.filter(criteria);

        // assert
        assertThat(filteredBatteries).isEmpty();
    }

    @Test
    @DisplayName("Should handle null criteria values")
    void shouldHandleNullCriteriaValues() {
        // arrange
        var criteria = BatterySearchCriteria.builder().build();

        // act
        List<Battery> filteredBatteries = repository.filter(criteria);

        // assert
        assertAll("Null criteria validation",
                () -> assertThat(filteredBatteries).isNotEmpty(),
                () -> assertThat(filteredBatteries)
                        .isSortedAccordingTo((b1, b2) ->
                                b1.getName().compareToIgnoreCase(b2.getName()))
        );
    }
}