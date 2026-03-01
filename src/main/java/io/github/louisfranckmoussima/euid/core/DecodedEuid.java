package io.github.louisfranckmoussima.euid.core;

import java.time.Instant;
import java.util.UUID;

public final class DecodedEuid {

    private final UUID uuid;
    private final long timestamp;
    private final Instant instant;
    private final int region;
    private final int shard;
    private final int node;
    private final long sequence;

    public DecodedEuid(UUID uuid, long timestamp, Instant instant,
                       int region, int shard, int node, long sequence) {
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.instant = instant;
        this.region = region;
        this.shard = shard;
        this.node = node;
        this.sequence = sequence;
    }

    public UUID getUuid() { return uuid; }
    public long getTimestamp() { return timestamp; }
    public Instant getInstant() { return instant; }
    public int getRegion() { return region; }
    public int getShard() { return shard; }
    public int getNode() { return node; }
    public long getSequence() { return sequence; }

    @Override
    public String toString() {
        return "DecodedEuid{" +
                "uuid=" + uuid +
                ", timestamp=" + instant +
                ", region=" + region +
                ", shard=" + shard +
                ", node=" + node +
                ", sequence=" + sequence +
                '}';
    }
}
