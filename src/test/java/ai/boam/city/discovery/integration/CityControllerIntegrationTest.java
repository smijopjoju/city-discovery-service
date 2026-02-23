package ai.boam.city.discovery.integration;

import ai.boam.city.discovery.dto.NearbyCityResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CityControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private final RestClient restClient = RestClient.create();

    @Test
    void shouldFindNearbyCitiesFromDatabase() {
        // given
        final var lat = 40.6943;
        final var lng = -73.9249;
        final var limit = 10;
        final var url = "http://localhost:" + port + "/cds/v1/cities/nearby?lat=" + lat + "&lng=" + lng + "&limit=" + limit;

        // when
        final var response = restClient.get()
                .uri(url)
                .retrieve()
                .body(NearbyCityResponse.class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.cities()).isNotEmpty();

        // "Test City 1" is in the test-cities.csv at (40.6943, -73.9249)
        final var foundTestCity1 = response.cities().stream()
                .anyMatch(c -> c.name().equals("Test City 1"));

        assertThat(foundTestCity1).isTrue();
        
        // Verify distance for the exact same point is 0.0
        final var testCity1 = response.cities().stream()
                .filter(c -> c.name().equals("Test City 1"))
                .findFirst()
                .orElseThrow();
                
        assertThat(testCity1.distanceKm()).isEqualTo(0.0);
    }

    @Test
    void shouldReturnBadRequestForInvalidCoordinates() {
        // given
        final var lat = 95.0;
        final var lng = 0.0;
        final var url = "http://localhost:" + port + "/cds/v1/cities/nearby?lat=" + lat + "&lng=" + lng;

        // when
        final var response = restClient.get()
                .uri(url)
                .retrieve()
                .onStatus(status -> status.equals(HttpStatus.BAD_REQUEST), (request, res) -> {
                    // Do nothing, just want the status
                })
                .toBodilessEntity();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
