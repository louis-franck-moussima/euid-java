package io.github.louisfranckmoussima.euid.core;

import java.util.UUID;

public final class EuidBase58Codec {

    private static final char[] ALPHABET =
            "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();

    private static final int[] INDEXES = new int[128];

    static {
        for (int i = 0; i < INDEXES.length; i++) INDEXES[i] = -1;
        for (int i = 0; i < ALPHABET.length; i++) INDEXES[ALPHABET[i]] = i;
    }

    private EuidBase58Codec() {}

    // ============================================================
    // THREAD LOCAL BUFFERS (ZERO ALLOCATION)
    // ============================================================

    private static final ThreadLocal<byte[]> TL_BYTES =
            ThreadLocal.withInitial(() -> new byte[16]);

    private static final ThreadLocal<byte[]> TL_WORK =
            ThreadLocal.withInitial(() -> new byte[16]);

    private static final ThreadLocal<char[]> TL_CHARS =
            ThreadLocal.withInitial(() -> new char[22]);

    private static final ThreadLocal<byte[]> TL_DECODE_WORK =
            ThreadLocal.withInitial(() -> new byte[22]);

    // ============================================================
    // ENCODE (FIXED 22 CHARS)
    // ============================================================

    public static String encode(UUID uuid) {

        byte[] bytes = TL_BYTES.get();
        writeUuid(uuid, bytes);

        byte[] work = TL_WORK.get();
        System.arraycopy(bytes, 0, work, 0, 16);

        char[] out = TL_CHARS.get();

        int charPos = 22;

        // ALWAYS 22 iterations → fixed-length encoding
        for (int i = 0; i < 22; i++) {
            int mod = divmod58(work);
            out[--charPos] = ALPHABET[mod];
        }

        return new String(out, 0, 22);
    }

    private static void writeUuid(UUID uuid, byte[] buffer) {

        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();

        buffer[0]  = (byte)(msb >>> 56);
        buffer[1]  = (byte)(msb >>> 48);
        buffer[2]  = (byte)(msb >>> 40);
        buffer[3]  = (byte)(msb >>> 32);
        buffer[4]  = (byte)(msb >>> 24);
        buffer[5]  = (byte)(msb >>> 16);
        buffer[6]  = (byte)(msb >>> 8);
        buffer[7]  = (byte)(msb);

        buffer[8]  = (byte)(lsb >>> 56);
        buffer[9]  = (byte)(lsb >>> 48);
        buffer[10] = (byte)(lsb >>> 40);
        buffer[11] = (byte)(lsb >>> 32);
        buffer[12] = (byte)(lsb >>> 24);
        buffer[13] = (byte)(lsb >>> 16);
        buffer[14] = (byte)(lsb >>> 8);
        buffer[15] = (byte)(lsb);
    }

    private static int divmod58(byte[] number) {

        int remainder = 0;

        for (int i = 0; i < 16; i++) {

            int digit = number[i] & 0xFF;

            int temp = remainder * 256 + digit;

            number[i] = (byte)(temp / 58);

            remainder = temp % 58;
        }

        return remainder;
    }

    // ============================================================
    // DECODE (STRICT 22 CHARS)
    // ============================================================

    public static UUID decode(String base58) {

        if (base58.length() != 22)
            throw new IllegalArgumentException("Base58 UUID must be 22 chars");

        byte[] work = TL_DECODE_WORK.get();

        // Convert chars → base58 digits
        for (int i = 0; i < 22; i++) {

            char c = base58.charAt(i);

            if (c >= 128 || INDEXES[c] < 0)
                throw new IllegalArgumentException("Invalid Base58 char: " + c);

            work[i] = (byte) INDEXES[c];
        }

        byte[] out = TL_BYTES.get();
        // zero out
        for (int i = 0; i < 16; i++) out[i] = 0;

        int bytePos = 16;

        for (int i = 0; i < 16; i++) {
            int mod = divmod256(work);
            out[--bytePos] = (byte) mod;
        }

        long msb = 0;
        long lsb = 0;

        for (int i = 0; i < 8; i++)
            msb = (msb << 8) | (out[i] & 0xFF);

        for (int i = 8; i < 16; i++)
            lsb = (lsb << 8) | (out[i] & 0xFF);

        return new UUID(msb, lsb);
    }

    private static int divmod256(byte[] number) {

        int remainder = 0;

        for (int i = 0; i < 22; i++) {

            int digit = number[i] & 0xFF;

            int temp = remainder * 58 + digit;

            number[i] = (byte)(temp / 256);

            remainder = temp % 256;
        }

        return remainder;
    }
}

