package org.virtualpowerplant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "battery",
        indexes = {
                @Index(name = "idx_battery_postcode", columnList = "postcode"),
                @Index(name = "idx_battery_watt_capacity", columnList = "wattCapacity")
        }
)
public class Battery extends BaseEntity {
    @NotBlank
    @Column(nullable = false)
    String name;

    @Min(0)
    @Column(nullable = false)
    Integer postcode;

    @Min(0)
    @Column(name = "watt_capacity", nullable = false)
    Double wattCapacity;

}