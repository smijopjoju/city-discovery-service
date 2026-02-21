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
        String url = "http://localhost:" + port + "/cds/v1/cities/nearby?lat=40.6943&lng=-73.9249&limit=10";

        // when
        NearbyCityResponse response = restClient.get()
                .uri(url)
                .retrieve()
                .body(NearbyCityResponse.class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.cities()).isNotEmpty();

        // "New York" is the current dummy data returned by CityService
        boolean foundNewYork = response.cities().stream()
                .anyMatch(c -> c.name().equals("New York"));

        assertThat(foundNewYork).isTrue();
    }

    @Test
    void shouldReturnBadRequestForInvalidCoordinates() {
        // given
        String url = "http://localhost:" + port + "/cds/v1/cities/nearby?lat=95.0&lng=0.0";

        // when
        var response = restClient.get()
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
