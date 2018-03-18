package com.harium.etyl.networking.udp;

import com.harium.etyl.networking.BaseServerImpl;
import com.harium.etyl.networking.core.helper.ByteMessageHelper;
import com.harium.etyl.networking.core.model.BaseServer;
import com.harium.etyl.networking.core.model.Peer;
import com.harium.etyl.networking.core.model.data.ConnectionData;
import com.harium.etyl.networking.core.model.data.ConnectionType;
import com.harium.etyl.networking.core.model.data.RawData;
import com.harium.etyl.networking.core.protocol.Protocol;
import com.harium.etyl.networking.core.protocol.ProtocolHandler;

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
 * http://rox-xmlrpc.sourceforge.net/niotut/
 */
public class UDPServer extends BaseServerImpl implements BaseServer, Runnable {

    // Disconnect by inactivity
    private long MAX_IDLE = 10000; // 10 seconds
    protected boolean disconnectByInactivity = true;

    private static final int BUFFER_SIZE = 1024;
    public static final String CHARSET = "UTF-8";

    private int port;
    private static int count = 0;

    protected Map<Integer, Data> peers = new HashMap<Integer, Data>();
    protected Map<String, Data> connections = new HashMap<String, Data>();
    protected ByteBuffer req = ByteBuffer.allocate(BUFFER_SIZE);

    protected ConnectionData dataHolder = new ConnectionData();

    public UDPServer(int port) {
        super();
        this.port = port;
        dataHolder.connectionType = ConnectionType.UDP;
    }

    public Map<String, Data> getConnections() {
        return connections;
    }

    public Map<Integer, Data> getPeers() {
        return peers;
    }

    public class Data extends Peer {
        int id;
        long lastInteraction = 0;
        SocketAddress sa;
        DatagramChannel channel;
        ByteBuffer resp;

        List<byte[]> messages;
        SelectionKey key;

        public Data() {
            resp = ByteBuffer.allocate(BUFFER_SIZE);
            messages = new ArrayList<>();
        }

        @Override
        public int getId() {
            return id;
        }
    }

    class Connection {
        SocketAddress sa;
        DatagramChannel channel;
        ByteBuffer resp;

        public Connection() {
            resp = ByteBuffer.allocate(BUFFER_SIZE);
        }
    }

    public void start() {
        new Thread(this).start();
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
        byte[] array = new byte[req.position()];
        req.rewind();
        req.get(array);
        receive(connection.id, array);

        // Clear buffer
        req.clear();
    }

    private Data updateConnection(SelectionKey key) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        SocketAddress sa = channel.receive(req);

        String uniqueId = sa.toString();
        Data data;

        if (!connections.containsKey(uniqueId)) {
            data = new Data();
            data.id = count;
            data.sa = sa;
            data.channel = channel;
            data.key = key;
            data.lastInteraction = System.currentTimeMillis();

            // Add a new connection
            connections.put(uniqueId, data);
            peers.put(count, data);
            // TODO etyl-networking
            onConnect(data);

            System.out.println("Added " + count + ": " + uniqueId);
            count++;
        } else {
            data = connections.get(uniqueId);
        }

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
        Peer peer = getPeer(connectionId);

        serverHandler.receiveMessageData(peer, dataHolder);
        System.out.println("Receive: " + new String(message));
    }

    public void send(int connectionId, byte[] message) {
        Data connection = peers.get(connectionId);
        send(connection, message);
    }

    public void send(Data connection, byte[] message) {
        //Data connection = connections.get(connectionId);
        //connection.messages.add(message);
        connection.key.interestOps(SelectionKey.OP_WRITE);
        connection.resp = ByteBuffer.wrap(message);
        try {
            connection.channel.send(connection.resp, connection.sa);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Base Server Methods
    @Override
    public void onConnect(Peer peer) {
        serverHandler.addPeer(peer);
        joinPeer(peer);
    }

    @Override
    public void joinPeer(Peer peer) {

    }

    @Override
    public void leftPeer(Peer peer) {

    }

    @Override
    public boolean hasPeer(int id) {
        return peers.containsKey(id);
    }

    @Override
    public Peer getPeer(int id) {
        return peers.get(id);
    }

    @Override
    public void removePeer(int id) {
        Peer peer = getPeer(id);
        if (peer != null) {
            leftPeer(peer);
            serverHandler.removePeer(peer);
            peers.remove(id);
        }
    }

    @Override
    public void sendToUDP(int id, ConnectionData connectionData) {
        byte[] message = ByteMessageHelper.concatenate(connectionData.prefix, connectionData.data);
        send(id, message);
    }

    @Override
    public void sendToUDP(int id, RawData rawData) {
        send(id, rawData.data);
    }

    @Override
    public void sendToAllUDP(ConnectionData connectionData) {
        for (Integer id : peers.keySet()) {
            sendToUDP(id, connectionData);
        }
    }

    @Override
    public void sendToAllExceptUDP(int exceptId, ConnectionData connectionData) {
        for (Integer id : peers.keySet()) {
            if (exceptId == id) {
                continue;
            }
            sendToUDP(id, connectionData);
        }
    }

}