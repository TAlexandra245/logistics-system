package com.project.logistics.service;

import com.project.logistics.dao.Destination;
import com.project.logistics.dto.DestinationConverter;
import com.project.logistics.dto.DestinationDto;
import com.project.logistics.exceptions.CanNotCreateEntity;
import com.project.logistics.exceptions.ResourceNotFoundException;
import com.project.logistics.repository.DestinationRepository;
import com.project.logistics.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;
    private final OrderRepository orderRepository;

    public DestinationService(DestinationRepository destinationRepository, OrderRepository orderRepository) {
        this.destinationRepository = destinationRepository;
        this.orderRepository = orderRepository;
    }

    public List<DestinationDto> getAllDestinations() {
        List<Destination> destinationList = destinationRepository.findAll();
        return DestinationConverter.entityListToDtoList(destinationList);
    }

    public DestinationDto findById(Long id) throws ResourceNotFoundException {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found"));
        return DestinationConverter.entityToDto(destination);
    }

    public void deleteDestination(Long id) throws ResourceNotFoundException {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found"));
        orderRepository.findAllByDestinationId(id).forEach(orderRepository::archiveOrder);

        destinationRepository.delete(destination);
    }

    public Long createDestination(DestinationDto destinationDto) throws CanNotCreateEntity{
        if(destinationDto.getId() != null) {
            throw new CanNotCreateEntity("Id should not be provided");
        }
        Optional<Destination> optionalDestination = destinationRepository.findDestinationByName(destinationDto.getName());
        if(optionalDestination.isPresent()) {
            Destination destination = optionalDestination.get();
            throw new CanNotCreateEntity(String.format("Destination with id=%id already has name=%s", destination.getId(), destination.getName()));
        }

        Destination destination = DestinationConverter.dtoToEntity(destinationDto);

        destinationRepository.save(destination);

        return destination.getId();
    }
}
