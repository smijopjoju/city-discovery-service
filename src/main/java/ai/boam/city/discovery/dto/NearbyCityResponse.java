package ai.boam.city.discovery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Response containing a list of nearby cities based on search criteria")
public record NearbyCityResponse(
    @Schema(description = "Total number of cities found within criteria", example = "10")
    Integer totalFound,
    
    @Schema(description = "The original search center point")
    SearchPoint searchPoint,
    
    @Schema(description = "List of nearby cities found")
    List<NearCityInfo> cities
) {}
