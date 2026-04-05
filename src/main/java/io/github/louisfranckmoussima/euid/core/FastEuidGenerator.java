package io.github.louisfranckmoussima.euid.core;

import java.util.UUID;

public final class FastEuidGenerator extends AbstractEuidGenerator {

    private long lastTimestamp = -1L;
    private long sequence = 0L;

    private final long[] buffer = new long[2];

    public FastEuidGenerator(int region, int shard, int node) {
        super(region, shard, node);
    }

    @Override
    public UUID generate() {
        long now = currentTimestamp();

        if (now < lastTimestamp) {
            now = waitNextMillis(lastTimestamp);
        }

        if (now == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0L) {
                now = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = now;

        buffer[0] = buildMsb(now);
        buffer[1] = buildLsb(sequence);

        return new UUID(buffer[0], buffer[1]);
    }
}