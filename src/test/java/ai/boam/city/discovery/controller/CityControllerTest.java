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

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
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
        final var searchPoint = new SearchPoint(40.7128, -74.0060);
        final var cityInfo = new NearCityInfo("New York", "NY", 40.6943, -73.9249, 1.25);
        final var response = new NearbyCityResponse(1, searchPoint, List.of(cityInfo));

        when(cityService.findNearbyCities(anyDouble(), anyDouble(), anyInt())).thenReturn(response);

        // when & then
        mockMvc.perform(get("/cds/v1/cities/nearby")
                        .param("lat", "40.7128")
                        .param("lng", "-74.0060")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalFound").value(1))
                .andExpect(jsonPath("$.searchPoint.lat").value(40.7128))
                .andExpect(jsonPath("$.cities[0].name").value("New York"));
    }

    @Test
    void getNearbyCities_ShouldReturnBadRequestForInvalidCoordinates() throws Exception {
        // when & then
        mockMvc.perform(get("/cds/v1/cities/nearby")
                        .param("lat", "91.0")
                        .param("lng", "-74.0060"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getNearbyCities_ShouldReturnBadRequestForInvalidLimit() throws Exception {
        // when & then
        mockMvc.perform(get("/cds/v1/cities/nearby")
                        .param("lat", "40.7128")
                        .param("lng", "-74.0060")
                        .param("limit", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getNearbyCities_ShouldReturnBadRequestForMissingParams() throws Exception {
        // when & then
        mockMvc.perform(get("/cds/v1/cities/nearby"))
                .andExpect(status().isBadRequest());
    }
}
