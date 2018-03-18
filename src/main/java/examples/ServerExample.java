package examples;

import com.harium.etyl.networking.udp.UDPServer;

import java.util.Iterator;
import java.util.Map;

public class ServerExample {

    static UDPServer server;

    public static void main(String[] args) {
        System.out.println("Start Server");

        int port = ClientExample.PORT;

        server = new UDPServer(port);
        server.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                poll();
            }
        }).start();
    }

    private static void poll() {
        int count = 0;
        while (true) {
            if (!server.getPeers().isEmpty()) {
                long now = System.currentTimeMillis();
                System.out.println("Users: " + server.getPeers().size());

                for (Iterator<Map.Entry<String, UDPServer.Data>> it = server.getConnections().entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<String, UDPServer.Data> entry = it.next();
                    //int id = entry.getKey();
                    UDPServer.Data connection = entry.getValue();

                    // Remove connection due to inactivity
                    /*if (disconnectByInactivity && connection.lastInteraction + MAX_IDLE < now) {
                        //peers.remove(getUniqueId(connection));
                        it.remove();
                        continue;
                    }*/

                    String message = ClientExample.PREFIX_CLIENT + " To " + connection.getId() + ": Hello " + count;
                    count++;
                    server.send(connection, message.getBytes());
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
