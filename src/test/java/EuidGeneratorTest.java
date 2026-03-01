import io.github.louisfranckmoussima.euid.core.DecodedEuid;
import io.github.louisfranckmoussima.euid.core.EuidBase58Codec;
import io.github.louisfranckmoussima.euid.core.EuidDecoder;
import io.github.louisfranckmoussima.euid.core.EuidGenerator;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class EuidGeneratorTest {

    // Basic Generation Test
    @Test
    void shouldGenerateNonNullUuid() {
        EuidGenerator generator = new EuidGenerator(1, 1, 1);
        UUID id = generator.generate();

        assertNotNull(id);
    }


    // Decode Round-Trip Test
    @Test
    void shouldDecodeCorrectly() {
        EuidGenerator generator = new EuidGenerator(2, 3, 4);
        UUID id = generator.generate();

        DecodedEuid decoded = EuidDecoder.decode(id);

        assertEquals(2, decoded.getRegion());
        assertEquals(3, decoded.getShard());
        assertEquals(4, decoded.getNode());
    }

    // Base58 Round-Trip Test
    @Test
    void base58ShouldRoundTrip() {
        EuidGenerator generator = new EuidGenerator(1,1,1);

        UUID id = generator.generate();
        String encoded = EuidBase58Codec.encode(id);
        UUID decoded = EuidBase58Codec.decode(encoded);

        assertEquals(id, decoded);
    }

    // Validation test

    @Test
    void shouldRejectInvalidRegion() {
        assertThrows(IllegalArgumentException.class,
                () -> new EuidGenerator(100, 0, 0));
    }
}
