package com.harium.etyl.networking.udp.utils;

import org.junit.Test;

import static com.harium.etyl.networking.udp.utils.ByteUtils.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ByteUtilsTest {

    @Test
    public void testIntToByteArray() {
        assertArrayEquals(new byte[]{0, 0, 0, 2}, intToByteArray(2));
    }

    @Test
    public void testIntToByteArrayAndBack() {
        int number = 2;
        byte[] converted = intToByteArray(number);
        assertArrayEquals(new byte[]{0, 0, 0, 2}, converted);

        int back = byteArrayToInt(converted);
        assertEquals(number, back);
    }

    @Test
    public void testBigIntToByteArrayAndBack() {
        int number = 512;
        byte[] converted = intToByteArray(number);
        assertArrayEquals(new byte[]{0, 0, 2, 0}, converted);

        int back = byteArrayToInt(converted);
        assertEquals(number, back);
    }

    @Test
    public void testByteArrayToIntWithIndex() {
        int back = byteArrayToInt(new byte[]{1, 1, 1, 0, 0, 0, 2}, 3);
        assertEquals(2, back);

        //number = 512;

        back = byteArrayToInt(new byte[]{1, 1, 1, 0, 0, 2, 0}, 3);
        assertEquals(512, back);
    }

    @Test
    public void givenAFloatthenConvertToByteArray() {
        assertArrayEquals(new byte[]{63, -116, -52, -51}, floatToByteArray(1.1f));
    }

    @Test
    public void givenAByteArray_thenConvertToFloat() {
        assertEquals(1.1f, byteArrayToFloat(new byte[]{63, -116, -52, -51}), 0);
    }

}
