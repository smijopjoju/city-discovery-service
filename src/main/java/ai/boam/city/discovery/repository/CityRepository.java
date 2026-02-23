package ai.boam.city.discovery.repository;

import ai.boam.city.discovery.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Stream<City> findByLatitudeBetweenAndLongitudeBetween(final Double latStart, final Double latEnd, final Double lngStart, final Double lngEnd);
}
