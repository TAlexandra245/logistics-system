package com.project.logistics.controller;

import com.project.logistics.dao.Destination;
import com.project.logistics.dto.DestinationDto;
import com.project.logistics.exceptions.CanNotCreateEntity;
import com.project.logistics.exceptions.ResourceNotFoundException;
import com.project.logistics.service.DestinationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get a destination by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the destination",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Destination.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Destination not found",
                    content = @Content)})
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
    public DestinationDto updateDestination(@RequestBody @Valid DestinationDto destinationDto) throws CanNotCreateEntity {
        return destinationService.updateDestination(destinationDto);
    }

}
