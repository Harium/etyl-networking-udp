package com.harium.etyl.networking.udp.model;

import com.harium.etyl.networking.udp.UDPServer;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Connection {
    public SocketAddress sa;
    public DatagramChannel channel;

    public ByteBuffer resp;

    public Connection() {
        resp = ByteBuffer.allocate(UDPServer.BUFFER_SIZE);
    }
}