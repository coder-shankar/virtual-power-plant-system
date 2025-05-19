package org.virtualpowerplant.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.virtualpowerplant.entity.Battery;
import org.virtualpowerplant.model.BatteryRequestDto;
import org.virtualpowerplant.model.BatteryResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BatteryMapper {
    BatteryMapper INSTANCE = Mappers.getMapper(BatteryMapper.class);

    Battery toEntity(BatteryRequestDto requestDto);

    BatteryResponseDto toResponse(Battery entity);
}
