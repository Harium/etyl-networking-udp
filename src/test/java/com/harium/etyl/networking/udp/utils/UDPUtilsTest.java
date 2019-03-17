package com.harium.etyl.networking.udp.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static com.harium.etyl.networking.udp.utils.UDPUtils.splitMessages;

public class UDPUtilsTest {

    @Test
    public void testBuildMessage() {
        Assert.assertArrayEquals(new byte[]{0, 0, 2, 0}, UDPUtils.buildMessage("Hello".getBytes()));
    }

    @Test
    public void testRemoveHeader() {
        Assert.assertArrayEquals(new byte[]{99}, UDPUtils.removeHeader(new byte[]{0, 0, 2, 0, 99}));
    }

    @Test
    public void testRemoveHeaderFromConcatMessage() {
        byte[] message = new byte[]{0, 0, 0, 1, 99, 0, 0, 0, 1, 88};
        Assert.assertArrayEquals(new byte[]{99}, UDPUtils.removeHeader(0, 1, message));
        Assert.assertArrayEquals(new byte[]{88}, UDPUtils.removeHeader(5, 1, message));
    }

    @Test
    public void testReadSingleMessage() {
        byte[] message = UDPUtils.buildMessage("Hello".getBytes());
        List<byte[]> messages = splitMessages(message);

        Assert.assertEquals(1, messages.size());
        Assert.assertArrayEquals("Hello".getBytes(), messages.get(0));
    }

    @Test
    public void testReadMultipleMessages() {
        byte[] a = UDPUtils.buildMessage("Hello".getBytes());
        byte[] b = UDPUtils.buildMessage("World".getBytes());
        byte[] message = UDPUtils.concat(a, b);

        List<byte[]> messages = splitMessages(message);

        Assert.assertEquals(2, messages.size());
        Assert.assertArrayEquals("Hello".getBytes(), messages.get(0));
        Assert.assertArrayEquals("World".getBytes(), messages.get(1));
    }

}
