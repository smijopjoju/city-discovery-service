package ai.boam.city.discovery.repository;

import ai.boam.city.discovery.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByLatitudeBetweenAndLongitudeBetween(Double latStart, Double latEnd, Double lngStart, Double lngEnd);
}
