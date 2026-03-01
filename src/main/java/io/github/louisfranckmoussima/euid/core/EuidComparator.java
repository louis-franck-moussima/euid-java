package io.github.louisfranckmoussima.euid.core;

import java.util.Comparator;
import java.util.UUID;

public final class EuidComparator {

    private EuidComparator() {}

    public static final Comparator<UUID> TIME_ORDERED = (u1, u2) -> {
        long msb1 = u1.getMostSignificantBits();
        long msb2 = u2.getMostSignificantBits();

        if (msb1 != msb2) {
            return Long.compareUnsigned(msb1, msb2);
        }

        long lsb1 = u1.getLeastSignificantBits();
        long lsb2 = u2.getLeastSignificantBits();

        return Long.compareUnsigned(lsb1, lsb2);
    };

    public static final Comparator<UUID> TIMESTAMP_ONLY = (u1, u2) -> {
        long t1 = (u1.getMostSignificantBits() >>> 16) & 0xFFFFFFFFFFFFL;
        long t2 = (u2.getMostSignificantBits() >>> 16) & 0xFFFFFFFFFFFFL;
        return Long.compare(t1, t2);
    };
}
