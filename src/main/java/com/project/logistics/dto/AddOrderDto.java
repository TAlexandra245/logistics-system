package com.project.logistics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class AddOrderDto {

    @NotNull
    private Long deliveryDate;

    @NotNull
    private Long destinationId;
}
