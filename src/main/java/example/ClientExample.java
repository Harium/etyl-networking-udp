package example;

import java.util.ArrayList;

public class ClientExample {

    public static final String HOST = "localhost";
    public static final int PORT = 8340;

    public static void main(String[] args) throws InterruptedException {
        SimpleClient client = new SimpleClient(HOST, PORT);
        client.start();

        log("Connecting to Server on port:" + PORT);

        ArrayList<String> messages = new ArrayList<String>();

        messages.add("Hello");
        messages.add("World");
        messages.add("or");
        messages.add("Mars");

        messages.add("Hello");
        messages.add("Mars");
        messages.add("or");
        messages.add("World");

        for (String message : messages) {
            log("Sending: " + message);
            client.send(message.getBytes());

            // wait for 2 seconds before sending next message
            Thread.sleep(1000);
        }

        Thread.sleep(2000);
        client.close();
    }

    private static void log(String str) {
        System.out.println(str);
    }

}