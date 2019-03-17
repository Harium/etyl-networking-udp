package example;

import com.harium.etyl.networking.udp.UDPClient;

public class SimpleClient extends UDPClient {

    public SimpleClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected void onReceive(byte[] messageBytes) {
        String message = new String(messageBytes);
        System.out.println("Received(" + messageBytes.length + "): " + message);
    }

}
