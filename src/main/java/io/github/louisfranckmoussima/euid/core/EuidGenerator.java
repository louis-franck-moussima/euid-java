package io.github.louisfranckmoussima.euid.core;

import java.util.UUID;

/***
 * UUID wrapper (allocation only when needed)
 */

public final class EuidGenerator {

    private final EuidCoreGenerator core;
    private static final ThreadLocal<long[]> TL_LONGS =
            ThreadLocal.withInitial(() -> new long[2]);

    public EuidGenerator(int region, int shard, int node) {

        validate(region, shard, node);

        this.core = new EuidCoreGenerator(region, shard, node);
    }

    // ------------------------------------------------
    // Standard UUID generation
    // ------------------------------------------------
    public UUID generate() {

        long[] buf = TL_LONGS.get();

        core.generate(buf);

        return new UUID(buf[0], buf[1]);
    }

    private static void validate(int region, int shard, int node) {

        if (region < 0 || region > 63)
            throw new IllegalArgumentException("Region must be between 0 and 63 (6 bits)");

        if (shard < 0 || shard > 63)
            throw new IllegalArgumentException("Shard must be between 0 and 63 (6 bits)");

        if (node < 0 || node > 16383)
            throw new IllegalArgumentException("Node must be between 0 and 16383 (14 bits)");
    }

}



