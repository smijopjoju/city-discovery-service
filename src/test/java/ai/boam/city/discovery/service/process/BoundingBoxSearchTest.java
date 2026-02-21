package ai.boam.city.discovery.service.process;

import ai.boam.city.discovery.entity.City;
import ai.boam.city.discovery.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoundingBoxSearchTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private BoundingBoxSearch searcher;

    @Test
    void apply_ShouldReturnSortedAndLimitedCandidates() {
        // given
        final var lat = 40.7128;
        final var lng = -74.0060;
        final var limit = 1;
        final var context = DiscoveryContext.builder(lat, lng, limit).build();

        final var city1 = createCity("New York", "NY", 40.6943, -73.9249); // Closer
        final var city2 = createCity("Philadelphia", "PA", 39.9526, -75.1652); // Further

        when(cityRepository.findByLatitudeBetweenAndLongitudeBetween(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(Stream.of(city1, city2));

        // when
        final var result = searcher.apply(context);

        // then
        assertNotNull(result);
        assertEquals(1, result.candidates().size());
        assertEquals("New York", result.candidates().get(0).name());
        assertTrue(result.candidates().get(0).distanceKm() > 0);
    }

    @Test
    void calculateDistance_ShouldBeAccurateForSamePoint() {
        // given
        final var lat = 40.7128;
        final var lng = -74.0060;

        // when
        final var distance = BoundingBoxSearch.calculateDistance(lat, lng, lat, lng);

        // then
        assertEquals(0.0, distance);
    }

    private City createCity(final String name, final String stateId, final double lat, final double lng) {
        final var city = new City();
        city.setCity(name);
        city.setStateId(stateId);
        city.setLatitude(lat);
        city.setLongitude(lng);
        return city;
    }
}
