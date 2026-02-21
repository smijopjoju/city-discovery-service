package ai.boam.city.discovery.service.process;

import ai.boam.city.discovery.dto.NearCityInfo;
import ai.boam.city.discovery.entity.City;
import ai.boam.city.discovery.repository.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Searches for cities within a bounding box around the given point.
 */
@Component
public class BoundingBoxSearch implements DiscoveryStep {

    private static final Logger log = LoggerFactory.getLogger(BoundingBoxSearch.class);
    private final CityRepository cityRepository;

    public BoundingBoxSearch(final CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DiscoveryContext apply(final DiscoveryContext context) {
        final var box = calculateBoundingBox(context.lat(), context.lng());
        log.info("Searching cities in bounding box: lat[{} to {}], lng[{} to {}]",
                box.latStart(), box.latEnd(), box.lngStart(), box.lngEnd());

        try (final Stream<City> cityStream = cityRepository.findByLatitudeBetweenAndLongitudeBetween(
                box.latStart(), box.latEnd(), box.lngStart(), box.lngEnd())) {

            final var candidates = cityStream
                    .map(city -> new NearCityInfo(
                            city.getCity(),
                            city.getStateId(),
                            city.getLatitude(),
                            city.getLongitude(),
                            calculateDistance(context.lat(), context.lng(), city.getLatitude(), city.getLongitude())
                    ))
                    .sorted(Comparator.comparingDouble(NearCityInfo::distanceKm))
                    .limit(context.limit())
                    .toList();

            log.info("Found {} city candidates within bounding box for [{}, {}]", candidates.size(), context.lat(), context.lng());

            return DiscoveryContext.builder(context.lat(), context.lng(), context.limit())
                    .candidates(candidates)
                    .build();
        }
    }

    private BoundingBox calculateBoundingBox(final double lat, final double lng) {
        // One degree of latitude is approximately 111.32 km.
        // We use a 1.0 degree offset for latitude.
        double latOffset = 1.0;

        // Longitude degree distance shrinks as we move away from the equator.
        double lngOffset = 1.0 / Math.cos(Math.toRadians(lat));

        return new BoundingBox(
                lat - latOffset, lat + latOffset,
                lng - lngOffset, lng + lngOffset
        );
    }

    public static double calculateDistance(final double lat1, final double lon1, final double lat2, final double lon2) {
        final int R = 6371; // Radius of the earth in km
        final double latDistance = Math.toRadians(lat2 - lat1);
        final double lonDistance = Math.toRadians(lon2 - lon1);
        final double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round(R * c * 100.0) / 100.0; // Round to 2 decimal places
    }

    private record BoundingBox(double latStart, double latEnd, double lngStart, double lngEnd) {
    }
}
