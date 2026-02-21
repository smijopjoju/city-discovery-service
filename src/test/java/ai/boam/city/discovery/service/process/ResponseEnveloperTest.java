package ai.boam.city.discovery.service.process;

import ai.boam.city.discovery.dto.NearCityInfo;
import ai.boam.city.discovery.dto.SearchPoint;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResponseEnveloperTest {

    private final ResponseEnveloper enveloper = new ResponseEnveloper();

    @Test
    void apply_ShouldEnvelopeCandidatesIntoResponse() {
        // given
        final var lat = 40.7128;
        final var lng = -74.0060;
        final var limit = 10;
        final var cityInfo = new NearCityInfo("New York", "NY", 40.6943, -73.9249, 1.25);
        final var context = DiscoveryContext.builder(lat, lng, limit)
                .candidates(List.of(cityInfo))
                .build();

        // when
        final var result = enveloper.apply(context);

        // then
        assertNotNull(result.response());
        assertEquals(1, result.response().totalFound());
        assertEquals(new SearchPoint(lat, lng), result.response().searchPoint());
        assertEquals(List.of(cityInfo), result.response().cities());
    }
}
