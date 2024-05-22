package com.project.logistics.controller;

import com.project.logistics.dto.DestinationDto;
import com.project.logistics.exceptions.CanNotCreateEntity;
import com.project.logistics.exceptions.ResourceNotFoundException;
import com.project.logistics.service.DestinationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinations")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationService destinationService;

    @GetMapping
    public List<DestinationDto> getAllDestinations() {

        return destinationService.getAllDestinations();
    }

    @GetMapping("/{id}")
    public DestinationDto getDestination(@PathVariable Long id) throws ResourceNotFoundException {
        return destinationService.findById(id);
    }

    @DeleteMapping("{id}")
    public void deleteDestination(@PathVariable Long id) throws ResourceNotFoundException {
        destinationService.deleteDestination(id);
    }

    @PostMapping("/add")
    public String createDestination(@RequestBody @Valid DestinationDto destinationDto) throws CanNotCreateEntity {
        return destinationService.createDestination(destinationDto);
    }

    @PutMapping
    public void updateDestination(@RequestBody @Valid DestinationDto destinationDto) throws CanNotCreateEntity {
        destinationService.updateDestination(destinationDto);
    }

}
