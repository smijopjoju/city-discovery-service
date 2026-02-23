package ai.boam.city.discovery.initializer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"city", "city_ascii", "state_id", "state_name", "lat", "lng"})
public record CityCsvRecord(
    String city,
    String cityAscii,
    String stateId,
    String stateName,
    Double latitude,
    Double longitude
) {
    @JsonCreator
    public CityCsvRecord(
        @JsonProperty("city") String city,
        @JsonProperty("city_ascii") String cityAscii,
        @JsonProperty("state_id") String stateId,
        @JsonProperty("state_name") String stateName,
        @JsonProperty("lat") Double latitude,
        @JsonProperty("lng") Double longitude
    ) {
        this.city = city;
        this.cityAscii = cityAscii;
        this.stateId = stateId;
        this.stateName = stateName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
