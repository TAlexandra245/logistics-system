package com.project.logistics.service;

import com.project.logistics.cache.DestinationCache;
import com.project.logistics.dao.Destination;
import com.project.logistics.dto.DestinationDto;
import com.project.logistics.exceptions.CanNotCreateEntity;
import com.project.logistics.exceptions.ResourceNotFoundException;
import com.project.logistics.repository.DestinationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//@SpringBootTest(classes = {DestinationRepository.class, DestinationCache.class, OrderService.class})
@SpringBootTest
@ActiveProfiles("test")
class DestinationServiceTest {

    @Autowired
    DestinationService destinationService;

    @Autowired
    DestinationRepository destinationRepository;

    @Mock
    DestinationCache destinationCache;
    DestinationDto destinationDto = DestinationDto.builder()
            .name("Ploiesti")
            .distance(20)
            .build();

    Destination destination = Destination.builder()
            .id(1L)
            .name("Ploiesti")
            .distance(20)
            .build();

    @Test
    public void testDeleteById_NotFound() {
        //given
        Long idToDelete = destinationService.getAllDestinations().stream().mapToLong(DestinationDto::getId).max().orElse(0) + 1;

        //when
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> destinationService.deleteDestination(idToDelete));

        //then
        assertEquals("Destination not found for id " + idToDelete, resourceNotFoundException.getMessage());
    }

    @Test
    public void testDeleteById_Ok() throws ResourceNotFoundException {
        //given
        Long idToDelete = destinationService.getAllDestinations().stream().mapToLong(DestinationDto::getId).findAny().orElseThrow();

        Optional<Destination> foundDestination = destinationRepository.findById(idToDelete);
        assertTrue(foundDestination.isPresent());
        //when
        destinationService.deleteDestination(idToDelete);

        //then
        foundDestination = destinationRepository.findById(idToDelete);
        assertFalse(foundDestination.isPresent());

    }

    @Test
    public void testCreateDestination_AlreadyExists() {
        String destinationName = "Ploiesti";

        when(destinationCache.findByName(destinationName)).thenReturn(Optional.of(destination));

        CanNotCreateEntity canNotCreateEntity = assertThrows(CanNotCreateEntity.class, () -> destinationService.createDestination(destinationDto));

        assertEquals("Destination with id 1 already has name=Ploiesti", canNotCreateEntity.getMessage());
    }

    @Test
    public void testCreateDestination_WithIdProvided() {
        destinationDto.setId(1L);

        CanNotCreateEntity canNotCreateEntity = assertThrows(CanNotCreateEntity.class, () -> destinationService.createDestination(destinationDto));

        assertEquals("Id should not be provided", canNotCreateEntity.getMessage());
    }

    @Test
    public void testCreateDestination_Ok() throws CanNotCreateEntity {
        DestinationDto newDestination = DestinationDto.builder()
                .name("Galati")
                .distance(433)
                .build();

        when(destinationCache.findByName(newDestination.getName())).thenReturn(Optional.empty());


        String message = destinationService.createDestination(newDestination);

        assertEquals("Entity with id 11 has been created successfully", message);
    }

}