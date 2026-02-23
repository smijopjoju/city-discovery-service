package ai.boam.city.discovery.initializer;

import ai.boam.city.discovery.entity.City;
import ai.boam.city.discovery.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CityDataLoaderTest {

    private final String testCsvPath = "test-cities.csv";

    @Mock
    private CityRepository cityRepository;

    private CityDataLoader cityDataLoader;

    @BeforeEach
    void setUp() {
        cityDataLoader = new CityDataLoader(cityRepository, testCsvPath);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldLoadDataFromCsvWhenDatabaseIsEmpty() throws Exception {
        // given
        when(cityRepository.count()).thenReturn(0L);

        // when
        cityDataLoader.run();

        // then
        final var captor = ArgumentCaptor.forClass(List.class);
        verify(cityRepository, times(1)).saveAll(captor.capture());

        final var savedCities = (List<City>) captor.getValue();
        assertThat(savedCities).hasSize(2);
        assertThat(savedCities.get(0).getCity()).isEqualTo("Test City 1");
        assertThat(savedCities.get(1).getCity()).isEqualTo("Test City 2");
    }

    @Test
    void shouldNotLoadDataWhenDatabaseIsNotEmpty() throws Exception {
        // given
        when(cityRepository.count()).thenReturn(5L);

        // when
        cityDataLoader.run();

        // then
        verify(cityRepository, never()).saveAll(anyList());
    }
}
