package io.github.louisfranckmoussima.euid.core;

abstract class AbstractEuidGenerator implements EuidGenerator {

    protected static final long TIMESTAMP_MASK = 0xFFFFFFFFFFFFL;
    protected static final long SEQUENCE_MASK  = 0xFFFFFFFFFFFFL;

    protected final int region;
    protected final int shard;
    protected final int node;

    protected final long fixedMsbLow;
    protected final long fixedLsbHigh;

    protected AbstractEuidGenerator(int region, int shard, int node) {
        EuidValidation.validate(region, shard, node);
        this.region = region;
        this.shard = shard;
        this.node = node;

        this.fixedMsbLow =
                (0x8L << 12)
                        | (((long) region & 0x3FL) << 6)
                        | ((long) shard & 0x3FL);

        this.fixedLsbHigh =
                (0x2L << 62)
                        | (((long) node & 0x3FFFL) << 48);
    }

    protected final long buildMsb(long timestamp) {
        return ((timestamp & TIMESTAMP_MASK) << 16) | fixedMsbLow;
    }

    protected final long buildLsb(long sequence) {
        return fixedLsbHigh | (sequence & SEQUENCE_MASK);
    }

    protected final long currentTimestamp() {
        return System.currentTimeMillis() & TIMESTAMP_MASK;
    }

    protected final long waitNextMillis(long lastTimestamp) {
        long now;
        do {
            Thread.onSpinWait();
            now = currentTimestamp();
        } while (now <= lastTimestamp);
        return now;
    }
}

