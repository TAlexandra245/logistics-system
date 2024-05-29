package com.project.logistics.service;

import com.project.logistics.cache.DestinationCache;
import com.project.logistics.dao.Destination;
import com.project.logistics.dto.DestinationConverter;
import com.project.logistics.dto.DestinationDto;
import com.project.logistics.exceptions.CanNotCreateEntity;
import com.project.logistics.exceptions.ResourceNotFoundException;
import com.project.logistics.repository.DestinationRepository;
import com.project.logistics.repository.OrderRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final DestinationRepository destinationRepository;
    private final OrderRepository orderRepository;

    private final DestinationCache destinationCache;

    public List<DestinationDto> getAllDestinations() {
        List<Destination> destinationList = destinationRepository.findAll();
        return DestinationConverter.entityListToDtoList(destinationList);
    }

    public DestinationDto findById(Long id) throws ResourceNotFoundException {
        Destination destination = destinationCache.findById(id).orElseThrow(() -> new ResourceNotFoundException("Destination not found"));
        return DestinationConverter.entityToDto(destination);
    }

    @CacheEvict("destinations")
    public void deleteDestination(Long id) throws ResourceNotFoundException {
        Destination destination = destinationCache.findById(id).orElseThrow(() -> new ResourceNotFoundException("Destination not found"));
        orderRepository.findAllByDestinationId(id).forEach(orderRepository::archiveOrder);

        destinationRepository.delete(destination);
        destinationCache.delete(destination);
    }

    public String createDestination(DestinationDto destinationDto) throws CanNotCreateEntity {
        if (destinationDto.getId() != null) {
            throw new CanNotCreateEntity("Id should not be provided");
        }

        Optional<Destination> optionalDestination = destinationCache.findByName(destinationDto.getName());

        if (optionalDestination.isPresent()) {
            Destination destination = optionalDestination.get();
            throw new CanNotCreateEntity(String.format("Destination with id %d already has name=%s", destination.getId(), destination.getName()));
        }

        Destination destination = DestinationConverter.dtoToEntity(destinationDto);
        destinationRepository.save(destination);
        destinationCache.put(destination);

        return String.format("Entity with id %d has been created successfully", destination.getId());
    }

    @CachePut(value = "destinations", key = "#destinationDto.id")
    public DestinationDto updateDestination(DestinationDto destinationDto) throws CanNotCreateEntity {
        if (destinationDto.getId() == null) {
            throw new CanNotCreateEntity("Id should be provided");
        }

        Optional<Destination> optionalDestination = destinationCache.findByName(destinationDto.getName());
        if (optionalDestination.isPresent()) {
            Destination destination = optionalDestination.get();
            if (!Objects.equals(destination.getId(), destinationDto.getId())) {
                throw new CanNotCreateEntity(String.format("Destination with id %d already has name %s", destination.getId(), destination.getName()));
            }
        }

        Destination destination = DestinationConverter.dtoToEntity(destinationDto);
        Destination saved = destinationRepository.save(destination);
        destinationCache.put(saved);
        return DestinationConverter.entityToDto(saved);

    }
}
