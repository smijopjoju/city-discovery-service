package ai.boam.city.discovery.service.process;

import ai.boam.city.discovery.dto.NearbyCityResponse;
import ai.boam.city.discovery.dto.SearchPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Envelopes the results into a standardized response DTO.
 */
@Component
public class ResponseEnveloper implements DiscoveryStep {

    private static final Logger log = LoggerFactory.getLogger(ResponseEnveloper.class);

    @Override
    public DiscoveryContext apply(final DiscoveryContext context) {
        final var finalResponse = new NearbyCityResponse(
                context.candidates().size(),
                new SearchPoint(context.lat(), context.lng()),
                context.candidates()
        );

        log.info("Enveloped {} cities for response to [{}, {}]", finalResponse.totalFound(), context.lat(), context.lng());

        return DiscoveryContext.builder(context.lat(), context.lng(), context.limit())
                .candidates(context.candidates())
                .response(finalResponse)
                .build();
    }
}
