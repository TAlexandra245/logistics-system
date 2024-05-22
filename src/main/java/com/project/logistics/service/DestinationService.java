package com.project.logistics.service;

import com.project.logistics.dao.Destination;
import com.project.logistics.dto.DestinationConverter;
import com.project.logistics.dto.DestinationDto;
import com.project.logistics.exceptions.CanNotCreateEntity;
import com.project.logistics.exceptions.ResourceNotFoundException;
import com.project.logistics.repository.DestinationRepository;
import com.project.logistics.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final DestinationRepository destinationRepository;
    private final OrderRepository orderRepository;

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

    public String createDestination(DestinationDto destinationDto) throws CanNotCreateEntity {
        if (destinationDto.getId() != null) {
            throw new CanNotCreateEntity("Id should not be provided");
        }

        Optional<Destination> optionalDestination = destinationRepository.findDestinationByName(destinationDto.getName());

        if (optionalDestination.isPresent()) {
            Destination destination = optionalDestination.get();
            throw new CanNotCreateEntity(String.format("Destination with id %d already has name=%s", destination.getId(), destination.getName()));
        }

        Destination destination = DestinationConverter.dtoToEntity(destinationDto);
        destinationRepository.save(destination);

        return String.format("Entity with id %d has been created successfully", destination.getId());
    }

    public void updateDestination(DestinationDto destinationDto) throws CanNotCreateEntity {
        if (destinationDto.getId() == null) {
            throw new CanNotCreateEntity("Id should be provided");
        }

        Optional<Destination> optionalDestination = destinationRepository.findDestinationByName(destinationDto.getName());
        if (optionalDestination.isPresent()) {
            Destination destination = optionalDestination.get();
            if (!Objects.equals(destination.getId(), destinationDto.getId())) {
                throw new CanNotCreateEntity(String.format("Destination with id %d already has name %s", destination.getId(), destination.getName()));
            }
        }

        Destination destination = DestinationConverter.dtoToEntity(destinationDto);
        destinationRepository.save(destination);
    }
}
