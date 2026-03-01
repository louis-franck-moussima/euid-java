package io.github.louisfranckmoussima.euid.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public final class EuidBatchGenerator {

    private final EuidGenerator generator;
    public EuidBatchGenerator(EuidGenerator generator) {
        this.generator = generator;
    }


    public List<UUID> generateBatch(int n) {
        List<UUID> ids = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            ids.add(generator.generate());
        }
        return ids;
    }


}

