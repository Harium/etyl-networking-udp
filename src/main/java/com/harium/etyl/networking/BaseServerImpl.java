package com.harium.etyl.networking;

import com.harium.etyl.networking.core.model.BaseServer;
import com.harium.etyl.networking.core.model.ByteArrayKey;
import com.harium.etyl.networking.core.model.data.ConnectionData;
import com.harium.etyl.networking.core.model.data.RawData;
import com.harium.etyl.networking.core.protocol.Protocol;
import com.harium.etyl.networking.core.protocol.ProtocolHandler;

import java.util.Map;

public abstract class BaseServerImpl implements BaseServer {
    protected ProtocolHandler serverHandler;

    public BaseServerImpl() {
        this.serverHandler = new ProtocolHandler();
    }

    public void setHandshaker(Protocol handshaker) {
        serverHandler.handshaker = handshaker;
    }

    @Override
    public void sendToTCP(int id, ConnectionData connectionData) {
        // Do nothing
    }

    @Override
    public void sendToTCP(int id, RawData rawData) {
        // Do nothing
    }

    @Override
    public void sendToAllTCP(ConnectionData connectionData) {
        // Do nothing
    }

    @Override
    public void sendToAllExceptTCP(int id, ConnectionData connectionData) {
        // Do nothing
    }

    /**
     * Adds the protocol with the default prefix
     *
     * @param prefix   - the prefix associated to the protocol
     * @param protocol - the protocol to respond by it's own prefix
     */
    public void addProtocol(byte[] prefix, Protocol protocol) {
        serverHandler.addProtocol(prefix, protocol);
    }

    public void addProtocol(String prefix, Protocol protocol) {
        this.addProtocol(prefix.getBytes(), protocol);
    }

    public void addProtocol(Protocol protocol) {
        this.addProtocol(protocol.getPrefix(), protocol);
    }

    public Map<ByteArrayKey, Protocol> getProtocols() {
        return serverHandler.getProtocols();
    }

    public Protocol getProtocol(String prefix) {
        return serverHandler.getProtocol(prefix);
    }
}
