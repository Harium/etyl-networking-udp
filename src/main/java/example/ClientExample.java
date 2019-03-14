package example;

import com.harium.etyl.networking.udp.UDPClient;

import java.io.IOException;
import java.util.ArrayList;

public class ClientExample {

    public static final String HOST = "localhost";
    public static final int PORT = 8340;

    public static void main(String[] args) throws IOException, InterruptedException {
        UDPClient client = new UDPClient(HOST, PORT);
        client.start();

        log("Connecting to Server on port:" + PORT);

        ArrayList<String> messages = new ArrayList<String>();

        messages.add("Hello");
        messages.add("World");
        messages.add("or");
        messages.add("Mars");

        for (String message : messages) {
            log("Sending: " + message);
            client.send(message.getBytes());

            // wait for 2 seconds before sending next message
            Thread.sleep(2000);
        }

        Thread.sleep(5000);
        client.close();
    }

    private static void log(String str) {
        System.out.println(str);
    }

}