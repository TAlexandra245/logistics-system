package com.project.logistics.controller;

import com.project.logistics.dto.DestinationDto;
import com.project.logistics.exceptions.ResourceNotFoundException;
import com.project.logistics.service.DestinationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping
    public List<DestinationDto> getAllDestinations() {

        return  destinationService.getAllDestinations();
    }

    @GetMapping("/{id}")
    public DestinationDto getDestination(@PathVariable Long id) throws ResourceNotFoundException {
        return destinationService.findById(id);
    }

}
