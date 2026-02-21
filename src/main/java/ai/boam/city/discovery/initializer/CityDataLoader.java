package ai.boam.city.discovery.initializer;

import ai.boam.city.discovery.entity.City;
import ai.boam.city.discovery.repository.CityRepository;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;

@Component
public class CityDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CityDataLoader.class);
    private final CityRepository cityRepository;
    private final String dataFilePath;

    public CityDataLoader(
            final CityRepository cityRepository,
            @Value("${app.city-data.path:uscities.csv}") final String dataFilePath
    ) {
        this.cityRepository = cityRepository;
        this.dataFilePath = dataFilePath;
    }

    @Override
    @Transactional
    public void run(final String... args) throws Exception {
        if (cityRepository.count() > 0) {
            log.info("Database already contains data, skipping CSV import.");
            return;
        }

        log.info("Starting CSV data import from {}...", dataFilePath);

        final var mapper = new CsvMapper();
        final var schema = mapper.schemaFor(CityCsvRecord.class).withHeader();

        try (final InputStream is = new ClassPathResource(dataFilePath).getInputStream()) {
            final MappingIterator<CityCsvRecord> it = mapper.readerFor(CityCsvRecord.class)
                    .with(schema)
                    .readValues(is);

            final var cities = new ArrayList<City>();
            while (it.hasNext()) {
                final var record = it.next();
                final var city = new City();
                city.setCity(record.city());
                city.setCityAscii(record.cityAscii());
                city.setStateId(record.stateId());
                city.setStateName(record.stateName());
                city.setLatitude(record.latitude());
                city.setLongitude(record.longitude());
                cities.add(city);
            }
            cityRepository.saveAll(cities);
            log.info("Successfully imported {} cities.", cities.size());
        } catch (final Exception e) {
            log.error("Error during CSV import from {}: ", dataFilePath, e);
            throw e;
        }
    }
}
