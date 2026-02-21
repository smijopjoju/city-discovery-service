package ai.boam.city.discovery.service;

import ai.boam.city.discovery.dto.NearbyCityResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @InjectMocks
    private CityService cityService;

    @Test
    void findNearbyCities_ShouldReturnMockData() {
        //given
        Double lat = 40.7128;
        Double lng = -74.0060;
        Integer limit = 10;

        //when
        NearbyCityResponse response = cityService.findNearbyCities(lat, lng, limit);

        //then
        assertNotNull(response);
        assertEquals(1, response.totalFound());
        assertEquals(lat, response.searchPoint().lat());
        assertEquals(lng, response.searchPoint().lng());
        assertEquals("New York", response.cities().get(0).name());
        assertEquals("NY", response.cities().get(0).stateId());
    }
}
