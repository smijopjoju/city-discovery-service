package ai.boam.city.discovery.integration;

import ai.boam.city.discovery.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CityDataLoaderIntegrationTest {

    @Autowired
    private CityRepository cityRepository;

    @Test
    void shouldProperlyInsertDataIntoDatabaseFromTestCsv() {
        // Assert: Data should be loaded automatically by Spring during startup with "test" profile
        final var cities = cityRepository.findAll();
        
        // We expect 2 cities from test-cities.csv
        assertThat(cities).hasSize(2);
        
        final var city1 = cities.stream()
                .filter(c -> c.getCity().equals("Test City 1"))
                .findFirst();
                
        assertThat(city1).isPresent();
        assertThat(city1.get().getStateId()).isEqualTo("NY");
        assertThat(city1.get().getLatitude()).isEqualTo(40.6943);
        assertThat(city1.get().getLongitude()).isEqualTo(-73.9249);
    }
}
