package com.project.logistics.dto;

import com.project.logistics.dao.Destination;

import java.util.List;

public class DestinationConverter {

    public static DestinationDto entityToDto(Destination destination) {
        return DestinationDto.builder()
                .id(destination.getId())
                .name(destination.getName())
                .distance(destination.getDistance())
                .build();
    }

    public static List<DestinationDto> entityListToDtoList(List<Destination> destinations) {
        return destinations.stream()
                .map(DestinationConverter::entityToDto)
                .toList();
    }

    public static Destination dtoToEntity(DestinationDto destinationDto) {
        return Destination.builder()
                .id(destinationDto.getId())
                .distance(destinationDto.getDistance())
                .name(destinationDto.getName())
                .build();
    }

    public static List<Destination> dtoListToEntityList(List<DestinationDto> destinationDtos) {
        return destinationDtos.stream().map(DestinationConverter::dtoToEntity).toList();
    }
}
