package ai.boam.city.discovery.controller;

import ai.boam.city.discovery.dto.NearbyCityResponse;
import ai.boam.city.discovery.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller providing city discovery endpoints.
 */
@RestController
@RequestMapping("/cds/v1/cities")
@Tag(name = "City Discovery", description = "Discover nearby urban hubs for data agent tasking.")
public class CityController {

    private static final Logger log = LoggerFactory.getLogger(CityController.class);
    private final CityService cityService;

    public CityController(final CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * Endpoint to find nearby cities based on center point and limit.
     */
    @Operation(summary = "Find nearby cities", description = "Discover the closest urban hubs for data agent tasking.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully found cities"),
        @ApiResponse(responseCode = "400", description = "Missing parameters or coordinate values out of range"),
        @ApiResponse(responseCode = "500", description = "Unexpected system failure")
    })
    @GetMapping("/nearby")
    public ResponseEntity<NearbyCityResponse> getNearbyCities(
            @Parameter(description = "Latitude of the center point (Range: -90 to 90).", example = "40.7128")
            @RequestParam(required = true) final Double lat,
            
            @Parameter(description = "Longitude of the center point (Range: -180 to 180).", example = "-74.0060")
            @RequestParam(required = true) final Double lng,
            
            @Parameter(description = "Number of cities to return. Default is 10, max is 50.", example = "20")
            @RequestParam(defaultValue = "10") final Integer limit) {

        log.info("Received discovery request: lat={}, lng={}, limit={}", lat, lng, limit);

        final var response = cityService.findNearbyCities(lat, lng, limit);
        return ResponseEntity.ok(response);
    }
}
