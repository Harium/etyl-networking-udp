package com.harium.etyl.networking.udp;

public class ServerExample {

    public static void main(String[] args) {
        System.out.println("Start Server");

        int port = ClientExample.PORT;

        UDPServer server = new UDPServer(port);
        server.start();
    }

}
