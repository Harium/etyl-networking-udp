package example;

import com.harium.etyl.networking.udp.UDPServer;
import com.harium.etyl.networking.udp.model.Data;

public class SimpleServer extends UDPServer {

    public SimpleServer(int port) {
        super(port);
    }

    @Override
    protected void onJoin(int uniqueId) {
        int count = connections.size();
        System.out.println("Added " + count + ": " + uniqueId);
    }

    @Override
    protected void onLeft(int uniqueId) {
        int count = connections.size();
        System.out.println("Removed " + count + ": " + uniqueId);
    }

    @Override
    protected void onMessage(int connectionId, byte[] message) {
        System.out.println("Receive (" + connectionId + "): " + new String(message));
    }

    int pollCount = 0;

    @Override
    protected void poll() {
        pollCount++;
        // Fill the queue
        for (Data connection : connections.values()) {
            String raw = "Hello (" + pollCount + "): " + connection.id;
            byte[] message = raw.getBytes();
            queue.put(connection, message);
        }
    }
}
