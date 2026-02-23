package ai.boam.city.discovery.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Information about a city near the search point")
public record NearCityInfo(
    @Schema(description = "Name of the city", example = "New York")
    String name,
    
    @Schema(description = "Two-letter state ID", example = "NY")
    String stateId,
    
    @Schema(description = "Latitude of the city", example = "40.6943")
    Double latitude,
    
    @Schema(description = "Longitude of the city", example = "-73.9249")
    Double longitude,
    
    @Schema(description = "Distance from the search point in kilometers", example = "1.25")
    Double distanceKm
) {}
