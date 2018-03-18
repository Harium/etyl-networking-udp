package com.harium.etyl.networking;

import com.harium.etyl.networking.core.model.BaseClient;
import com.harium.etyl.networking.core.model.Peer;
import com.harium.etyl.networking.core.model.data.ConnectionData;
import com.harium.etyl.networking.core.model.data.RawData;
import com.harium.etyl.networking.core.protocol.Protocol;
import com.harium.etyl.networking.core.protocol.ProtocolHandler;

public abstract class BaseClientImpl implements BaseClient {

    public static final Peer SERVER = new Peer() {
        @Override
        public int getId() {
            return Integer.MIN_VALUE;
        }
    };

    protected ProtocolHandler protocolHandler = new ProtocolHandler();

    /**
     * Adds the protocol with the default prefix
     *
     * @param prefix   - the prefix associated to the protocol
     * @param protocol - the protocol to respond by it's own prefix
     */
    public void addProtocol(byte[] prefix, Protocol protocol) {
        protocolHandler.addProtocol(prefix, protocol);
        protocol.addPeer(SERVER);
    }

    public void addProtocol(String prefix, Protocol protocol) {
        this.addProtocol(prefix.getBytes(), protocol);
    }

    public void addProtocol(Protocol protocol) {
        this.addProtocol(protocol.getPrefix(), protocol);
    }

    @Override
    public void sendToTCP(ConnectionData connectionData) {
        // Do nothing
    }

    @Override
    public void sendToTCP(RawData rawData) {
        // Do nothing
    }

    @Override
    public void sendToUDP(ConnectionData connectionData) {
        // Do nothing
    }

    @Override
    public void sendToUDP(RawData rawData) {
        // Do nothing
    }

    @Override
    public void sendToWebSocket(ConnectionData connectionData) {
        // Do nothing
    }

    @Override
    public void sendToWebSocket(RawData rawData) {
        // Do nothing
    }

}
