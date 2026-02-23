package ai.boam.city.discovery.service.process;

import ai.boam.city.discovery.dto.NearCityInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DiscoveryContextTest {

    @Test
    void builder_ShouldCreateContextWithProvidedValues() {
        // given
        final var lat = 40.7128;
        final var lng = -74.0060;
        final var limit = 10;

        // when
        final var context = DiscoveryContext.builder(lat, lng, limit).build();

        // then
        assertEquals(lat, context.lat());
        assertEquals(lng, context.lng());
        assertEquals(limit, context.limit());
        assertTrue(context.candidates().isEmpty());
    }

    @Test
    void builder_ShouldHandleCandidatesAndResponseUpdates() {
        // given
        final var lat = 40.7128;
        final var lng = -74.0060;
        final var limit = 10;
        final var cityInfo = new NearCityInfo("New York", "NY", 40.6943, -73.9249, 1.25);
        final var candidates = List.of(cityInfo);

        // when
        final var context = DiscoveryContext.builder(lat, lng, limit)
                .candidates(candidates)
                .build();

        // then
        assertNotNull(context.candidates());
        assertEquals(1, context.candidates().size());
        assertEquals("New York", context.candidates().get(0).name());
    }
}
