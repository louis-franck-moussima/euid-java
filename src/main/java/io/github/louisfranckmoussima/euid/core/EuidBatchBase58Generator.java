package io.github.louisfranckmoussima.euid.core;

import java.util.UUID;

public final class EuidBatchBase58Generator {

    private final EuidGenerator generator;

    public EuidBatchBase58Generator(EuidGenerator generator) {
        this.generator = generator;
    }

    public String[] generateBatch(int count) {

        String[] result = new String[count];

        for (int i = 0; i < count; i++) {

            UUID uuid = generator.generate();

            result[i] = EuidBase58Codec.encode(uuid);
        }

        return result;
    }
}
