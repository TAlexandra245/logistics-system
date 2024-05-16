package com.project.logistics.service;

import com.project.logistics.dao.Destination;
import com.project.logistics.dto.DestinationConverter;
import com.project.logistics.dto.DestinationDto;
import com.project.logistics.repository.DestinationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    public List<DestinationDto> getAllDestinations() {
        List<Destination> destinationList = destinationRepository.findAll();
        return DestinationConverter.entityListToDtoList(destinationList);
    }
}
