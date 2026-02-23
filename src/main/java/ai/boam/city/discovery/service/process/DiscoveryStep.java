package ai.boam.city.discovery.service.process;

import java.util.function.UnaryOperator;

/**
 * UnaryOperator<T> extends Function<T, T>, providing .andThen() out of the box.
 */
@FunctionalInterface
public interface DiscoveryStep extends UnaryOperator<DiscoveryContext> {
}
