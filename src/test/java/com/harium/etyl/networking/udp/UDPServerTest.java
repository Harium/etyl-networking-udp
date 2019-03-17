package com.harium.etyl.networking.udp;

import com.harium.etyl.networking.udp.utils.UDPUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class UDPServerTest {

    UDPServer server;

    @Before
    public void setUp() {
        server = new UDPServer(1234) {
            @Override
            protected void onLeft(int connectionId) {

            }

            @Override
            protected void onJoin(int connectionId) {

            }

            @Override
            protected void onMessage(int connectionId, byte[] message) {

            }

            @Override
            protected void poll() {

            }
        };
    }



}
