package io.github.louisfranckmoussima.euid.core;

import java.util.UUID;

public final class EuidBatchBase58Generator {

    private final EuidGenerator generator;

    public EuidBatchBase58Generator(EuidGenerator generator) {
        if (generator == null) {
            throw new IllegalArgumentException("generator must not be null");
        }
        this.generator = generator;
    }

    public String[] generateBatch(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count must be >= 0");
        }

        String[] result = new String[count];

        for (int i = 0; i < count; i++) {
            UUID uuid = generator.generate();
            result[i] = EuidBase58Codec.encode(uuid);
        }

        return result;
    }
}
