package com.project.logistics.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DestinationDto {
    private Long id;

    @NotBlank(message = "Name must be provided")
    private String name;

    @NotNull
    @Min(0)
    private int distance;

}
