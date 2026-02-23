package ai.boam.city.discovery.service;

import ai.boam.city.discovery.dto.NearbyCityResponse;
import ai.boam.city.discovery.service.process.BoundingBoxSearch;
import ai.boam.city.discovery.service.process.DiscoveryContext;
import ai.boam.city.discovery.service.process.RequestValidator;
import ai.boam.city.discovery.service.process.ResponseEnveloper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service to handle city discovery logic.
 */
@Service
public class CityService {

    private static final Logger log = LoggerFactory.getLogger(CityService.class);
    private final RequestValidator validator;
    private final BoundingBoxSearch searcher;
    private final ResponseEnveloper enveloper;

    public CityService(final RequestValidator validator, final BoundingBoxSearch searcher, final ResponseEnveloper enveloper) {
        this.validator = validator;
        this.searcher = searcher;
        this.enveloper = enveloper;
    }

    /**
     * Finds nearby cities for a given point.
     */
    public NearbyCityResponse findNearbyCities(final Double lat, final Double lng, final Integer limit) {
        
        final var initial = DiscoveryContext.builder(lat, lng, limit).build();

        // Chain the functions
        final var resultContext = validator
                .andThen(searcher)
                .andThen(enveloper)
                .apply(initial);
        log.info("Completed discovery pipeline for [{}, {}]", lat, lng);
        return resultContext.response();
    }
}
