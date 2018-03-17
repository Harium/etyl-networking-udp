package com.harium.etyl.networking.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.*;

/**
 * References:
 * http://tutorials.jenkov.com/java-nio/selectors.html
 * https://jfarcand.wordpress.com/2006/07/06/tricks-and-tips-with-nio-part-ii-why-selectionkey-attach-is-evil/
 */
public class UDPServer implements Runnable {

    private long MAX_IDLE = 10000; // 10 seconds
    private static final int BUFFER_SIZE = 1024;
    public static final String CHARSET = "UTF-8";
    protected boolean disconnectByInactivity = true;

    private int port;
    private static int count = 0;

    Map<Integer, Data> ids = new HashMap<Integer, Data>();
    Map<String, Data> connections = new HashMap<String, Data>();

    public UDPServer(int port) {
        this.port = port;
    }

    class Data extends Connection {
        int id;
        long lastInteraction = 0;
        List<byte[]> messages;

        public Data() {
            messages = new ArrayList<>();
        }
    }

    class Connection {
        SocketAddress sa;
        ByteBuffer req;
        ByteBuffer resp;
        DatagramChannel channel;

        public Connection() {
            req = ByteBuffer.allocate(BUFFER_SIZE);
            resp = ByteBuffer.allocate(BUFFER_SIZE);
        }
    }

    public void start() {
        new Thread(this).start();
        poll();
    }

    public void run() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        Selector selector = Selector.open();
        DatagramChannel channel = DatagramChannel.open();
        InetSocketAddress isa = new InetSocketAddress(port);
        channel.socket().bind(isa);
        channel.configureBlocking(false);
        SelectionKey clientKey = channel.register(selector, SelectionKey.OP_READ);
        clientKey.attach(new Connection());

        while (true) {
            process(selector);
        }
    }

    private void process(Selector selector) throws IOException {

        selector.select();
        Iterator selectedKeys = selector.selectedKeys().iterator();
        while (selectedKeys.hasNext()) {
            SelectionKey key = (SelectionKey) selectedKeys.next();
            selectedKeys.remove();

            if (!key.isValid()) {
                continue;
            }

            if (key.isReadable()) {
                read(key);
                key.interestOps(SelectionKey.OP_WRITE);
            } else if (key.isWritable()) {
                /*Connection connection = (Connection) key.attachment();

                ListIterator<byte[]> iterator = connection.messages.listIterator();
                while (iterator.hasNext()) {
                    byte[] message = iterator.next();
                    write(connection, message);
                    iterator.remove();
                }*/

                key.interestOps(SelectionKey.OP_READ);
            }
        }
    }

    private void read(SelectionKey key) throws IOException {
        Data connection = updateConnection(key);
        connection.lastInteraction = System.currentTimeMillis();

        // Read message
        byte[] array = new byte[connection.req.position()];
        connection.req.rewind();
        connection.req.get(array);
        receive(connection.id, array);

        // Clear buffer
        connection.req.clear();
    }

    private Data updateConnection(SelectionKey key) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        Connection connection = (Connection) key.attachment();

        ByteBuffer req = connection.req;
        SocketAddress sa = channel.receive(req);

        String uniqueId = sa.toString();
        Data data;

        if (!connections.containsKey(uniqueId)) {
            data = new Data();
            data.id = count;
            data.sa = sa;
            data.channel = channel;
            data.lastInteraction = System.currentTimeMillis();

            // Add a new connection
            connections.put(uniqueId, data);
            ids.put(count, data);

            System.out.println("Added " + count + ": " + uniqueId);
            count++;
        } else {
            data = connections.get(uniqueId);
        }

        data.req = req;

        return data;
    }

    private void write(Connection connection, String message) throws IOException {
        connection.resp = Charset.forName(CHARSET).newEncoder().encode(CharBuffer.wrap(message));
        connection.channel.send(connection.resp, connection.sa);
    }

    private void write(Connection connection, byte[] message) throws IOException {
        connection.resp = ByteBuffer.wrap(message);
        connection.channel.send(connection.resp, connection.sa);
    }

    public void receive(int connectionId, byte[] message) {
        System.out.println("Receive: " + new String(message));
    }

    public void send(int connectionId, byte[] message) throws IOException {
        Data connection = ids.get(connectionId);
        send(connection, message);
    }

    public void send(Data connection, byte[] message) throws IOException {
        //Data connection = connections.get(connectionId);
        //connection.messages.add(message);
        connection.resp = ByteBuffer.wrap(message);
        connection.channel.send(connection.resp, connection.sa);
    }

    private void poll() {
        int count = 0;
        while (true) {
            if (!connections.isEmpty()) {
                long now = System.currentTimeMillis();
                System.out.println("Users: " + connections.size());

                for (Iterator<Map.Entry<String, Data>> it = connections.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<String, Data> entry = it.next();
                    //int id = entry.getKey();
                    Data connection = entry.getValue();

                    // Remove connection due to inactivity
                    if (disconnectByInactivity && connection.lastInteraction + MAX_IDLE < now) {
                        //ids.remove(getUniqueId(connection));
                        it.remove();
                        continue;
                    }

                    String message = "To " + connection.id + ": Hello " + count;
                    count++;
                    try {
                        send(connection, message.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}