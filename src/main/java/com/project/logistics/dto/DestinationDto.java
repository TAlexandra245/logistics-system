package com.project.logistics.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DestinationDto {
    private Long id;

    @Schema(type = "string", example = "Ploiesti")
    @NotBlank(message = "Name must be provided")
    private String name;

    @Schema(type = "number", example = "50")
    @NotNull
    @Positive(message = "Distance must be > 0")
    private Integer distance;

}
