package com.project.logistics.service;

import com.project.logistics.cache.DestinationCache;
import com.project.logistics.dao.Destination;
import com.project.logistics.dto.DestinationDto;
import com.project.logistics.exceptions.CanNotCreateEntity;
import com.project.logistics.exceptions.ResourceNotFoundException;
import com.project.logistics.repository.DestinationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

//@SpringBootTest(classes = {DestinationRepository.class, DestinationCache.class, OrderService.class})
@SpringBootTest
@ActiveProfiles("test")
class DestinationServiceTest {

    @Autowired
    DestinationService destinationService;

    @Mock
    DestinationRepository destinationRepository;

    @Mock
    DestinationCache destinationCache;
    DestinationDto destinationDto;

    Destination destination;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        destinationDto = new DestinationDto();
        destinationDto.setName("Galati");
        destinationDto.setDistance(20);

        destination = new Destination(1L, "Ploiesti", 20);

    }

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

    @Order(2)
    @Test
    public void testDeleteById_Ok() throws ResourceNotFoundException {
        //given
        Long idToDelete = destinationService.getAllDestinations().stream().mapToLong(DestinationDto::getId).findAny().orElseThrow();

        Optional<Destination> destinationToBeFound = destinationRepository.findById(idToDelete);
        assertTrue(destinationToBeFound.isPresent());

        //when
        destinationService.deleteDestination(idToDelete);

        //then
        Optional<Destination> foundDestination = destinationRepository.findById(idToDelete);
        assertTrue(foundDestination.isEmpty());

    }

    @Test
    public void testCreateDestination_AlreadyExists() {
        when(destinationCache.findByName(anyString())).thenReturn(Optional.of(destination));

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
        when(destinationCache.findByName(anyString())).thenReturn(Optional.empty());
        when(destinationRepository.save(ArgumentMatchers.any(Destination.class))).thenReturn(destination);

        String message = destinationService.createDestination(destinationDto);

        assertEquals("Entity with id 11 has been created successfully", message);
    }

}