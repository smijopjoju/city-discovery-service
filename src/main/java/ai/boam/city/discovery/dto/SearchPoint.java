package ai.boam.city.discovery.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents the geographic center point for a search")
public record SearchPoint(
    @Schema(description = "Latitude of the center point", example = "40.7128")
    Double lat,
    
    @Schema(description = "Longitude of the center point", example = "-74.0060")
    Double lng
) {}
