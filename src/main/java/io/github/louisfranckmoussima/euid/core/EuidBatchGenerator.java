package io.github.louisfranckmoussima.euid.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public final class EuidBatchGenerator {

    private final EuidGenerator generator;

    public EuidBatchGenerator(EuidGenerator generator) {
        if (generator == null) {
            throw new IllegalArgumentException("generator must not be null");
        }
        this.generator = generator;
    }

    public List<UUID> generateBatch(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count must be >= 0");
        }

        List<UUID> ids = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            ids.add(generator.generate());
        }

        return ids;
    }
}

