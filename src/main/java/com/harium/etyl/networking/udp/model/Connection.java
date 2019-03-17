package com.harium.etyl.networking.udp.model;

import com.harium.etyl.networking.udp.UDPServer;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Connection {
    public SocketAddress sa;
    public DatagramChannel channel;

    public ByteBuffer response;

    public Connection() {
        response = ByteBuffer.allocate(UDPServer.BUFFER_SIZE);
    }
}