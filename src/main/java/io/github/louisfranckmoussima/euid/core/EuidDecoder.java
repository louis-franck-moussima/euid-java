package io.github.louisfranckmoussima.euid.core;


import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public final class EuidDecoder {

    private EuidDecoder() {}
    public static DecodedEuid decode(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();

        // -------------------------------
        // Safety checks
        // -------------------------------
        int version = (int) ((msb >>> 12) & 0xF);
        if (version != 8) {
            throw new IllegalArgumentException("Not a valid EUID (expected version 8)");
        }

        int variant = (int) ((lsb >>> 62) & 0x3);
        if (variant != 2) {
            throw new IllegalArgumentException("Invalid UUID variant");
        }

        // -------------------------------
        // Extract fields
        // -------------------------------
        long timestamp = (msb >>> 16) & 0xFFFFFFFFFFFFL;
        int region = (int) ((msb >>> 6) & 0x3FL);
        int shard = (int) (msb & 0x3FL);
        int node = (int) ((lsb >>> 48) & 0x3FFFL);
        long sequence = lsb & 0xFFFFFFFFFFFFL;

        Instant instant = Instant.ofEpochMilli(timestamp);

        return new DecodedEuid(uuid, timestamp, instant, region, shard, node, sequence);
    }

    /**
     * SQL helpers for indexing
     * Example usage:
     *   INSERT INTO table (id, region, shard, node, ts) VALUES (?, ?, ?, ?, ?)
     */
    public static Map<String, Object> toSqlColumns(UUID uuid) {
        DecodedEuid decoded = decode(uuid);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", uuid); // raw UUID
        map.put("timestamp", decoded.getInstant());
        map.put("region", decoded.getRegion());
        map.put("shard", decoded.getShard());
        map.put("node", decoded.getNode());
        map.put("sequence", decoded.getSequence());

        return map;
    }

    /**
     * Comparator to sort raw UUIDs chronologically
     */
    public static Comparator<UUID> comparator() {
        return (a, b) -> {
            long am = a.getMostSignificantBits();
            long bm = b.getMostSignificantBits();
            return Long.compareUnsigned(am, bm);
        };
    }
}
