package ai.boam.city.discovery.service;

import ai.boam.city.discovery.dto.NearCityInfo;
import ai.boam.city.discovery.dto.NearbyCityResponse;
import ai.boam.city.discovery.dto.SearchPoint;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service to handle city discovery logic.
 * Currently returns dummy data for architectural verification.
 */
@Service
public class CityService {
    
    /**
     * Finds nearby cities for a given point.
     * Returns dummy data for now.
     */
    public NearbyCityResponse findNearbyCities(Double lat, Double lng, Integer limit) {
        SearchPoint searchPoint = new SearchPoint(lat, lng);
        
        // Mock data
        List<NearCityInfo> mockCities = List.of(
            new NearCityInfo("New York", "NY", 40.6943, -73.9249, 1.25)
        );
        
        return new NearbyCityResponse(mockCities.size(), searchPoint, mockCities);
    }
}
