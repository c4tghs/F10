package distributedsystems.demo.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;

public class MessagePublisher {
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;

    public void multicast(String message, String multiCastAddress) throws IOException {
        socket = new DatagramSocket();
        group = InetAddress.getByName(multiCastAddress);
        buf = message.getBytes();

        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, group, 4446);
        socket.send(packet);
        socket.close();
    }

    public void unicast(String message, String unicastAddress) throws IOException {
        socket = new DatagramSocket();
        group = InetAddress.getByName(unicastAddress);
        buf = message.getBytes();

        System.out.println(message);
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, group, 4446);
        socket.send(packet);
        socket.close();
    }

}
