package com.harium.etyl.networking.udp.utils;

import java.util.ArrayList;
import java.util.List;

import static com.harium.etyl.networking.udp.utils.ByteUtils.byteArrayToInt;
import static com.harium.etyl.networking.udp.utils.ByteUtils.intToByteArray;

public class UDPUtils {

    public static final int HEADER_SIZE = 4;

    public static byte[] buildMessage(byte[] message) {
        byte[] header = buildHeader(message);
        byte[] cat = concat(header, message);

        return cat;
    }

    public static byte[] concat(byte[] a, byte[] b) {
        byte[] cat = new byte[a.length + b.length];
        System.arraycopy(a, 0, cat, 0, a.length);
        System.arraycopy(b, 0, cat, a.length, b.length);

        return cat;
    }

    public static byte[] removeHeader(byte[] message) {
        return removeHeader(0, message.length - HEADER_SIZE, message);
    }

    public static byte[] removeHeader(int index, int size, byte[] message) {
        byte[] cat = new byte[size];
        System.arraycopy(message, index + HEADER_SIZE, cat, 0, size);

        return cat;
    }

    private static byte[] buildHeader(byte[] message) {
        return intToByteArray(message.length);
    }

    public static List<byte[]> splitMessages(byte[] message) {
        // Turn into bucket (or add on a list based on connectioId)
        List<byte[]> messages = new ArrayList<>();

        int cursor = 0;

        while (message.length > cursor + HEADER_SIZE) {
            int size = byteArrayToInt(message, cursor);
            byte[] slice = UDPUtils.removeHeader(cursor, size, message);
            messages.add(slice);
            cursor += size + HEADER_SIZE;
        }

        return messages;
    }

}
