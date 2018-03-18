package com.harium.etyl.networking.udp;

import com.harium.etyl.networking.BaseClientImpl;
import com.harium.etyl.networking.core.helper.ByteMessageHelper;
import com.harium.etyl.networking.core.model.data.ConnectionData;
import com.harium.etyl.networking.core.model.data.ConnectionType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.List;

public class UDPClient extends BaseClientImpl {

    private ByteBuffer in = ByteBuffer.allocate(64);

    boolean connected = false;
    String host;
    int port;

    DatagramChannel channel;
    List<String> messages;
    protected ConnectionData dataHolder = new ConnectionData();

    public UDPClient(String host, int port) {
        this.host = host;
        this.port = port;
        dataHolder.connectionType = ConnectionType.UDP;
        messages = new ArrayList<>();
    }

    private void connect() throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(host, port);

        channel = DatagramChannel.open();
        channel.socket().connect(serverAddress);
        channel.configureBlocking(false);
        connected = true;
    }

    public void start() {
        if (connected) {
            return;
        }
        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    receive();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void send(byte[] message) {
        send(ByteBuffer.wrap(message));
    }

    public void send(ByteBuffer buffer) {
        try {
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receive() throws IOException, InterruptedException {
        while (connected) {
            channel.receive(in);

            if (in.position() > 0) {

                byte[] messageBytes = new byte[in.position()];
                in.rewind();
                in.get(messageBytes);

                dataHolder.prefix = ByteMessageHelper.getPrefix(messageBytes);
                dataHolder.data = ByteMessageHelper.wipePrefix(dataHolder.prefix, messageBytes);

                protocolHandler.receiveMessageData(SERVER, dataHolder);
                in.clear();
            }

            Thread.sleep(1000);
        }
    }

    public void close() {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connected = false;
    }

}