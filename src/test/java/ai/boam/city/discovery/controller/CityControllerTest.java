package ai.boam.city.discovery.controller;

import ai.boam.city.discovery.dto.NearCityInfo;
import ai.boam.city.discovery.dto.NearbyCityResponse;
import ai.boam.city.discovery.dto.SearchPoint;
import ai.boam.city.discovery.service.CityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CityController.class)
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CityService cityService;

    @Test
    void getNearbyCities_ShouldReturnSuccess() throws Exception {
        // given
        final var lat = 40.7128;
        final var lng = -74.0060;
        final var limit = 10;
        final var searchPoint = new SearchPoint(lat, lng);
        final var cityInfo = new NearCityInfo("New York", "NY", 40.6943, -73.9249, 1.25);
        final var response = new NearbyCityResponse(1, searchPoint, List.of(cityInfo));

        when(cityService.findNearbyCities(lat, lng, limit)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/cds/v1/cities/nearby")
                        .param("lat", String.valueOf(lat))
                        .param("lng", String.valueOf(lng))
                        .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalFound").value(1))
                .andExpect(jsonPath("$.searchPoint.lat").value(lat))
                .andExpect(jsonPath("$.cities[0].name").value("New York"));
    }

    @Test
    void getNearbyCities_ShouldReturnBadRequestForInvalidCoordinates() throws Exception {
        // given
        final var lat = 91.0;
        final var lng = -74.0060;
        final var limit = 10;

        when(cityService.findNearbyCities(lat, lng, limit))
                .thenThrow(new IllegalArgumentException("Latitude must be between -90 and 90."));

        // when & then
        mockMvc.perform(get("/cds/v1/cities/nearby")
                        .param("lat", String.valueOf(lat))
                        .param("lng", String.valueOf(lng))
                        .param("limit", String.valueOf(limit)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getNearbyCities_ShouldReturnBadRequestForInvalidLimit() throws Exception {
        // given
        final var lat = 40.7128;
        final var lng = -74.0060;
        final var limit = 0;

        when(cityService.findNearbyCities(lat, lng, limit))
                .thenThrow(new IllegalArgumentException("Limit must be between 1 and 50."));

        // when & then
        mockMvc.perform(get("/cds/v1/cities/nearby")
                        .param("lat", String.valueOf(lat))
                        .param("lng", String.valueOf(lng))
                        .param("limit", String.valueOf(limit)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getNearbyCities_ShouldReturnBadRequestForMissingParams() throws Exception {
        // when & then
        mockMvc.perform(get("/cds/v1/cities/nearby"))
                .andExpect(status().isBadRequest());
    }
}
