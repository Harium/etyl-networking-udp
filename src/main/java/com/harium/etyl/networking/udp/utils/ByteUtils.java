package com.harium.etyl.networking.udp.utils;

public class ByteUtils {

    /**
     * Code from: https://stackoverflow.com/q/5399798
     */
    public static int byteArrayToInt(byte[] b) {
        return byteArrayToInt(b, 0);
    }

    public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 4 - 1; i > 0; i--) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }

    public static byte[] intToByteArray(int a) {
        byte[] out = new byte[4];
        return intToByteArray(a, out);
    }

    public static byte[] intToByteArray(int a, byte[] out) {
        return intToByteArray(a, out, 0);
    }

    public static byte[] intToByteArray(int a, byte[] out, int offset) {
        out[offset] = (byte) ((a >> 24) & 0xFF);
        out[1 + offset] = (byte) ((a >> 16) & 0xFF);
        out[2 + offset] = (byte) ((a >> 8) & 0xFF);
        out[3 + offset] = (byte) (a & 0xFF);
        return out;
    }

    public static byte[] floatToByteArray(float f) {
        int intBits = Float.floatToIntBits(f);
        return new byte[]{
                (byte) (intBits >> 24), (byte) (intBits >> 16), (byte) (intBits >> 8), (byte) (intBits)};
    }

    public static float byteArrayToFloat(byte[] bytes) {
        return byteArrayToFloat(bytes, 0);
    }

    public static float byteArrayToFloat(byte[] bytes, int offset) {
        int intBits =
                bytes[offset] << 24 |
                        (bytes[1 + offset] & 0xFF) << 16 |
                        (bytes[2 + offset] & 0xFF) << 8 |
                        (bytes[3 + offset] & 0xFF);
        return Float.intBitsToFloat(intBits);
    }

}
