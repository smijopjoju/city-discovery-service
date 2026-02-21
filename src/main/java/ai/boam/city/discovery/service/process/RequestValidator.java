package ai.boam.city.discovery.service.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Validates the input parameters for the discovery request.
 */
@Component
public class RequestValidator implements DiscoveryStep {

    private static final Logger log = LoggerFactory.getLogger(RequestValidator.class);

    @Override
    public DiscoveryContext apply(final DiscoveryContext context) {
        log.info("Validating discovery request for [{}, {}]", context.lat(), context.lng());
        
        if (context.lat() < -90 || context.lat() > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90.");
        }
        if (context.lng() < -180 || context.lng() > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180.");
        }
        if (context.limit() < 1 || context.limit() > 50) {
            throw new IllegalArgumentException("Limit must be between 1 and 50.");
        }
        return context;
    }
}
