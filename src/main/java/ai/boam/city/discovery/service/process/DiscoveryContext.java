package ai.boam.city.discovery.service.process;

import ai.boam.city.discovery.dto.NearCityInfo;
import ai.boam.city.discovery.dto.NearbyCityResponse;

import java.util.List;

/**
 * The 'State' for a Pipeline Execution.
 */
public record DiscoveryContext(
        double lat,
        double lng,
        int limit,

        List<NearCityInfo> candidates,

        NearbyCityResponse response
) {
    public static Builder builder(final double lat, final double lng, final int limit) {
        return new Builder(lat, lng, limit);
    }

    // Builder to handle "Updates" between steps
    public static class Builder {
        private final double lat;
        private final double lng;
        private final int limit;
        private List<NearCityInfo> candidates = List.of();
        private NearbyCityResponse response;

        private Builder(final double lat, final double lng, final int limit) {
            this.lat = lat;
            this.lng = lng;
            this.limit = limit;
        }

        public Builder candidates(final List<NearCityInfo> candidates) {
            this.candidates = candidates;
            return this;
        }

        public Builder response(final NearbyCityResponse response) {
            this.response = response;
            return this;
        }

        public DiscoveryContext build() {
            return new DiscoveryContext(lat, lng, limit, candidates, response);
        }
    }
}
