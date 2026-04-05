package io.github.louisfranckmoussima.euid.core;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

final class ConcurrentEuidGenerator extends AbstractEuidGenerator {

    private static final int DEFAULT_BLOCK_SIZE = 1024;

    private final int blockSize;

    private final AtomicReference<State> state =
            new AtomicReference<>(new State(-1L, 0L));

    private final ThreadLocal<LocalContext> local =
            ThreadLocal.withInitial(LocalContext::new);

    private static final class State {
        final long timestamp;
        final long nextFreeSequence;

        State(long timestamp, long nextFreeSequence) {
            this.timestamp = timestamp;
            this.nextFreeSequence = nextFreeSequence;
        }
    }

    private static final class LocalContext {
        final long[] buffer = new long[2];
        long timestamp = -1L;
        long nextSequence = 0L;
        long limitExclusive = 0L;
    }

    public ConcurrentEuidGenerator(int region, int shard, int node) {
        this(region, shard, node, DEFAULT_BLOCK_SIZE);
    }

    public ConcurrentEuidGenerator(int region, int shard, int node, int blockSize) {
        super(region, shard, node);
        if (blockSize <= 0) {
            throw new IllegalArgumentException("blockSize must be > 0");
        }
        this.blockSize = blockSize;
    }

    @Override
    public UUID generate() {
        LocalContext ctx = local.get();

        if (ctx.nextSequence >= ctx.limitExclusive) {
            refillBlock(ctx);
        }

        long sequence = ctx.nextSequence++;
        long timestamp = ctx.timestamp;

        ctx.buffer[0] = buildMsb(timestamp);
        ctx.buffer[1] = buildLsb(sequence);

        return new UUID(ctx.buffer[0], ctx.buffer[1]);
    }

    private void refillBlock(LocalContext ctx) {
        while (true) {
            State current = state.get();

            long now = currentTimestamp();
            long timestamp = Math.max(now, current.timestamp);

            long start = (timestamp == current.timestamp)
                    ? current.nextFreeSequence
                    : 0L;

            long endExclusive = start + blockSize;

            if (endExclusive > (SEQUENCE_MASK + 1L)) {
                timestamp = waitNextMillis(timestamp);
                start = 0L;
                endExclusive = blockSize;
            }

            State next = new State(timestamp, endExclusive);

            if (state.compareAndSet(current, next)) {
                ctx.timestamp = timestamp;
                ctx.nextSequence = start;
                ctx.limitExclusive = endExclusive;
                return;
            }

            Thread.onSpinWait();
        }
    }
}
