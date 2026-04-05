package io.github.louisfranckmoussima.euid.core;

final class EuidValidation {

    private EuidValidation() {

    }

    static void validate(int region, int shard, int node) {
        if (region < 0 || region > 63) {
            throw new IllegalArgumentException("Region must be between 0 and 63 (6 bits)");
        }
        if (shard < 0 || shard > 63) {
            throw new IllegalArgumentException("Shard must be between 0 and 63 (6 bits)");
        }
        if (node < 0 || node > 16383) {
            throw new IllegalArgumentException("Node must be between 0 and 16383 (14 bits)");
        }
    }
}