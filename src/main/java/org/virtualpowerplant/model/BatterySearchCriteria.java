package org.virtualpowerplant.model;

import lombok.Builder;
import org.springframework.web.bind.annotation.RequestParam;

@Builder
public record BatterySearchCriteria(@RequestParam Integer startPostcode,
                                    @RequestParam Integer endPostcode,
                                    @RequestParam(required = false) Double minCapacity,
                                    @RequestParam(required = false) Double maxCapacity) {
}
