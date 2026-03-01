package io.github.louisfranckmoussima.euid.core;

import java.util.concurrent.atomic.AtomicReference;

public final class EuidCoreGenerator {

    private static final long TIMESTAMP_MASK = 0xFFFFFFFFFFFFL;
    private static final long SEQUENCE_MASK  = 0xFFFFFFFFFFFFL;

    private final int region;
    private final int shard;
    private final int node;

    private final AtomicReference<State> state =
            new AtomicReference<>(new State(-1L, 0L));

    private static final class State {
        final long timestamp;
        final long sequence;

        State(long timestamp, long sequence) {
            this.timestamp = timestamp;
            this.sequence = sequence;
        }
    }

    public EuidCoreGenerator(int region, int shard, int node) {
        validate(region, shard, node);
        this.region = region;
        this.shard = shard;
        this.node = node;
    }

    public void generate(long[] buffer) {

        while (true) {

            long now = System.currentTimeMillis() & TIMESTAMP_MASK;

            State current = state.get();
            long lastTimestamp = current.timestamp;

            if (now < lastTimestamp) {
                now = waitNextMillis(lastTimestamp);
            }

            final long nextSequence;
            if (now == lastTimestamp) {

                nextSequence = (current.sequence + 1) & SEQUENCE_MASK;

                if (nextSequence == 0) {
                    now = waitNextMillis(lastTimestamp);
                }

            } else {
                nextSequence = 0L;
            }

            State next = new State(now, nextSequence);

            if (state.compareAndSet(current, next)) {

                buffer[0] = buildMsb(now);
                buffer[1] = buildLsb(nextSequence);
                return;
            }

            Thread.onSpinWait();
        }
    }

    private long buildMsb(long timestamp) {
        return ((timestamp & TIMESTAMP_MASK) << 16)
                | (0x8L << 12)
                | (((long) region & 0x3FL) << 6)
                | ((long) shard & 0x3FL);
    }

    private long buildLsb(long sequence) {
        return (0x2L << 62)
                | (((long) node & 0x3FFFL) << 48)
                | (sequence & SEQUENCE_MASK);
    }

    private long waitNextMillis(long lastTimestamp) {
        long now;
        do {
            Thread.onSpinWait();
            now = System.currentTimeMillis() & TIMESTAMP_MASK;
        } while (now <= lastTimestamp);
        return now;
    }

    private static void validate(int region, int shard, int node) {

        if ((region & ~0x3F) != 0)
            throw new IllegalArgumentException("Invalid region");

        if ((shard & ~0x3F) != 0)
            throw new IllegalArgumentException("Invalid shard");

        if ((node & ~0x3FFF) != 0)
            throw new IllegalArgumentException("Invalid node");
    }
}
