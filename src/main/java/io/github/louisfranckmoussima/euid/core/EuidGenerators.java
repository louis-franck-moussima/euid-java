package io.github.louisfranckmoussima.euid.core;

import java.util.concurrent.atomic.AtomicReference;

public final class EuidGenerators {

    private EuidGenerators() {
    }

    public static EuidGenerator fast(int region, int shard, int node) {
        return new FastEuidGenerator(region, shard, node);
    }

    public static EuidGenerator concurrent(int region, int shard, int node) {
        return new ConcurrentEuidGenerator(region, shard, node);
    }

    public static EuidGenerator concurrent(int region, int shard, int node, int blockSize) {
        return new ConcurrentEuidGenerator(region, shard, node, blockSize);
    }

}
