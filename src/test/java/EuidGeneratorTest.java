import io.github.louisfranckmoussima.euid.core.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class EuidGeneratorTest {

    @Test
    void fastGeneratorShouldGenerateNonNullUuid() {
        EuidGenerator generator = EuidGenerators.fast(1, 1, 1);
        UUID id = generator.generate();

        assertNotNull(id);
    }

    @Test
    void concurrentGeneratorShouldGenerateNonNullUuid() {
        EuidGenerator generator = EuidGenerators.concurrent(1, 1, 1);
        UUID id = generator.generate();

        assertNotNull(id);
    }

    @Test
    void fastGeneratorShouldDecodeCorrectly() {
        EuidGenerator generator = EuidGenerators.fast(2, 3, 4);
        UUID id = generator.generate();

        DecodedEuid decoded = EuidDecoder.decode(id);

        assertEquals(2, decoded.getRegion());
        assertEquals(3, decoded.getShard());
        assertEquals(4, decoded.getNode());
    }

    @Test
    void concurrentGeneratorShouldDecodeCorrectly() {
        EuidGenerator generator = EuidGenerators.concurrent(2, 3, 4);
        UUID id = generator.generate();

        DecodedEuid decoded = EuidDecoder.decode(id);

        assertEquals(2, decoded.getRegion());
        assertEquals(3, decoded.getShard());
        assertEquals(4, decoded.getNode());
    }

    @Test
    void fastGeneratorBase58ShouldRoundTrip() {
        EuidGenerator generator = EuidGenerators.fast(1, 1, 1);

        UUID id = generator.generate();
        String encoded = EuidBase58Codec.encode(id);
        UUID decoded = EuidBase58Codec.decode(encoded);

        assertEquals(id, decoded);
    }

    @Test
    void concurrentGeneratorBase58ShouldRoundTrip() {
        EuidGenerator generator = EuidGenerators.concurrent(1, 1, 1);

        UUID id = generator.generate();
        String encoded = EuidBase58Codec.encode(id);
        UUID decoded = EuidBase58Codec.decode(encoded);

        assertEquals(id, decoded);
    }

    @Test
    void concurrentGeneratorShouldSupportCustomBlockSize() {
        EuidGenerator generator = EuidGenerators.concurrent(2, 3, 4, 2048);

        UUID id = generator.generate();
        DecodedEuid decoded = EuidDecoder.decode(id);

        assertNotNull(id);
        assertEquals(2, decoded.getRegion());
        assertEquals(3, decoded.getShard());
        assertEquals(4, decoded.getNode());
    }

    @Test
    void concurrentGeneratorWithCustomBlockSizeShouldRoundTripBase58() {
        EuidGenerator generator = EuidGenerators.concurrent(1, 1, 1, 2048);

        UUID id = generator.generate();
        String encoded = EuidBase58Codec.encode(id);
        UUID decoded = EuidBase58Codec.decode(encoded);

        assertEquals(id, decoded);
    }

    @Test
    void fastGeneratorShouldGenerateUniqueIdsInSingleThread() {
        EuidGenerator generator = EuidGenerators.fast(1, 1, 1);

        UUID first = generator.generate();
        UUID second = generator.generate();

        assertNotEquals(first, second);
    }

    @Test
    void concurrentGeneratorShouldGenerateUniqueIdsInSingleThread() {
        EuidGenerator generator = EuidGenerators.concurrent(1, 1, 1);

        UUID first = generator.generate();
        UUID second = generator.generate();

        assertNotEquals(first, second);
    }

    @Test
    void shouldRejectInvalidRegionForFastGenerator() {
        assertThrows(IllegalArgumentException.class,
                () -> EuidGenerators.fast(100, 0, 0));
    }

    @Test
    void shouldRejectInvalidRegionForConcurrentGenerator() {
        assertThrows(IllegalArgumentException.class,
                () -> EuidGenerators.concurrent(100, 0, 0));
    }

    @Test
    void shouldRejectInvalidShardForFastGenerator() {
        assertThrows(IllegalArgumentException.class,
                () -> EuidGenerators.fast(0, 100, 0));
    }

    @Test
    void shouldRejectInvalidShardForConcurrentGenerator() {
        assertThrows(IllegalArgumentException.class,
                () -> EuidGenerators.concurrent(0, 100, 0));
    }

    @Test
    void shouldRejectInvalidNodeForFastGenerator() {
        assertThrows(IllegalArgumentException.class,
                () -> EuidGenerators.fast(0, 0, 20000));
    }

    @Test
    void shouldRejectInvalidNodeForConcurrentGenerator() {
        assertThrows(IllegalArgumentException.class,
                () -> EuidGenerators.concurrent(0, 0, 20000));
    }

    @Test
    void concurrentGeneratorShouldRejectZeroBlockSize() {
        assertThrows(IllegalArgumentException.class,
                () -> EuidGenerators.concurrent(1, 1, 1, 0));
    }

    @Test
    void concurrentGeneratorShouldRejectNegativeBlockSize() {
        assertThrows(IllegalArgumentException.class,
                () -> EuidGenerators.concurrent(1, 1, 1, -1));
    }
}
