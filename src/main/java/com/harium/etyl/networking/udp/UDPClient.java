package com.harium.etyl.networking.udp;

import com.harium.etyl.networking.udp.utils.UDPUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.List;

import static com.harium.etyl.networking.udp.UDPServer.BUFFER_SIZE;

public abstract class UDPClient {

    private ByteBuffer in = ByteBuffer.allocate(BUFFER_SIZE);

    boolean connected = false;
    String host;
    int port;

    DatagramChannel channel;

    public UDPClient(String host, int port) {
        this.host = host;
        this.port = port;
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
        byte[] headerMessage = UDPUtils.buildMessage(message);
        send(ByteBuffer.wrap(headerMessage));
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

                List<byte[]> messages = UDPUtils.splitMessages(messageBytes);
                for (byte[] message : messages) {
                    onReceive(message);
                }

                in.clear();
            }

            Thread.sleep(1000);
        }
    }

    protected abstract void onReceive(byte[] message);

    public void close() {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connected = false;
    }

}