package ai.boam.city.discovery.service.process;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestValidatorTest {

    private final RequestValidator validator = new RequestValidator();

    @Test
    void apply_ShouldPassForValidCoordinates() {
        // given
        final var context = DiscoveryContext.builder(40.7128, -74.0060, 10).build();

        // when & then
        assertDoesNotThrow(() -> validator.apply(context));
    }

    @ParameterizedTest
    @CsvSource({
        "91.0, 0.0, 10, Latitude must be between -90 and 90.",
        "-91.0, 0.0, 10, Latitude must be between -90 and 90.",
        "0.0, 181.0, 10, Longitude must be between -180 and 180.",
        "0.0, -181.0, 10, Longitude must be between -180 and 180.",
        "40.0, -74.0, 0, Limit must be between 1 and 50.",
        "40.0, -74.0, 51, Limit must be between 1 and 50."
    })
    void apply_ShouldThrowExceptionForInvalidInputs(final double lat, final double lng, final int limit, final String expectedMessage) {
        // given
        final var context = DiscoveryContext.builder(lat, lng, limit).build();

        // when
        final var exception = assertThrows(IllegalArgumentException.class, () -> validator.apply(context));

        // then
        assertEquals(expectedMessage, exception.getMessage());
    }
}
