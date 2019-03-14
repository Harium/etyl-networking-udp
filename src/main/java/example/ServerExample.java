package example;

import com.harium.etyl.networking.udp.UDPServer;

public class ServerExample {

    public static void main(String[] args) {
        System.out.println("Start SimpleServer");

        int port = ClientExample.PORT;

        UDPServer server = new SimpleServer(port);
        server.start();
    }

}
