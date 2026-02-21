package ai.boam.city.discovery.service;

import ai.boam.city.discovery.dto.NearCityInfo;
import ai.boam.city.discovery.dto.NearbyCityResponse;
import ai.boam.city.discovery.dto.SearchPoint;
import ai.boam.city.discovery.service.process.BoundingBoxSearch;
import ai.boam.city.discovery.service.process.DiscoveryContext;
import ai.boam.city.discovery.service.process.RequestValidator;
import ai.boam.city.discovery.service.process.ResponseEnveloper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private RequestValidator validator;

    @Mock
    private BoundingBoxSearch searcher;

    @Mock
    private ResponseEnveloper enveloper;

    @InjectMocks
    private CityService cityService;

    @Test
    void findNearbyCities_ShouldOrchestratePipelineCorrectly() {
        // given
        final var lat = 40.7128;
        final var lng = -74.0060;
        final var limit = 10;
        final var cityInfo = new NearCityInfo("New York", "NY", 40.6943, -73.9249, 1.25);
        final var expectedResponse = new NearbyCityResponse(1, new SearchPoint(lat, lng), List.of(cityInfo));

        // Setup pipeline mock behavior to support andThen()
        when(validator.andThen(any())).thenCallRealMethod();

        when(validator.apply(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(searcher.apply(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(enveloper.apply(any())).thenAnswer(invocation -> {
            final DiscoveryContext context = invocation.getArgument(0);
            return DiscoveryContext.builder(context.lat(), context.lng(), context.limit())
                    .candidates(context.candidates())
                    .response(expectedResponse)
                    .build();
        });

        // when
        final var response = cityService.findNearbyCities(lat, lng, limit);

        // then
        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(validator).apply(any());
        verify(searcher).apply(any());
        verify(enveloper).apply(any());
    }
}
