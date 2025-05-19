package org.virtualpowerplant.util;

import org.virtualpowerplant.entity.Battery;
import org.virtualpowerplant.model.BatteryRequestDto;
import org.virtualpowerplant.model.BatteryResponseDto;
import org.virtualpowerplant.model.BatterySearchCriteria;

import java.util.List;
import java.util.UUID;

public class TestDataBuilder {

    public static class BatteryBuilder {
        public static Battery cannington() {
            return Battery.builder()
                          .name("Cannington")
                          .postcode(6107)
                          .wattCapacity(13500.0)
                          .build();
        }

        public static Battery midland() {
            return Battery.builder()
                          .name("Midland")
                          .postcode(6057)
                          .wattCapacity(50500.0)
                          .build();
        }

        public static Battery hayStreet() {
            return Battery.builder()
                          .name("Hay Street")
                          .postcode(6000)
                          .wattCapacity(23500.0)
                          .build();
        }

        public static List<Battery> allBatteries() {
            return List.of(cannington(), midland(), hayStreet());
        }
    }

    public static class BatteryRequestBuilder {
        public static BatteryRequestDto cannington() {
            return BatteryRequestDto.builder()
                                    .name("Cannington")
                                    .postcode(6107)
                                    .wattCapacity(13500.0)
                                    .build();
        }

        public static BatteryRequestDto midland() {
            return BatteryRequestDto.builder()
                                    .name("Midland")
                                    .postcode(6057)
                                    .wattCapacity(50500.0)
                                    .build();
        }

        public static BatteryRequestDto hayStreet() {
            return BatteryRequestDto.builder()
                                    .name("Hay Street")
                                    .postcode(6000)
                                    .wattCapacity(23500.0)
                                    .build();
        }

        public static List<BatteryRequestDto> allRequests() {
            return List.of(cannington(), midland(), hayStreet());
        }
    }

    public static class BatteryResponseBuilder {
        public static BatteryResponseDto cannington() {
            return new BatteryResponseDto(UUID.randomUUID(), "Cannington", 6107, 13500.0);
        }

        public static BatteryResponseDto midland() {
            return new BatteryResponseDto(UUID.randomUUID(), "Midland", 6057, 50500.0);
        }

        public static BatteryResponseDto hayStreet() {
            return new BatteryResponseDto(UUID.randomUUID(), "Hay Street", 6000, 23500.0);
        }

        public static List<BatteryResponseDto> allResponses() {
            return List.of(cannington(), midland(), hayStreet());
        }
    }

    public static class SearchCriteriaBuilder {
        public static BatterySearchCriteria defaultCriteria() {
            return BatterySearchCriteria.builder()
                                        .startPostcode(6000)
                                        .endPostcode(6999)
                                        .minCapacity(13500.0)
                                        .maxCapacity(50500.0)
                                        .build();
        }
    }

    // Utility constants
    public static final double TOTAL_CAPACITY = 87500.0; // 13500 + 50500 + 23500
    public static final double AVERAGE_CAPACITY = 29166.666666666668; // 87500 / 3
}