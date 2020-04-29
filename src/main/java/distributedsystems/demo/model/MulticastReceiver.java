package distributedsystems.demo.model;

import lombok.Data;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

@Data
public class MulticastReceiver extends Thread {

    private String message;
    private InetAddress senderAddress;

    private int port;
    private String multiCastAddress;

    @Override
    public void run() {
        try {
            byte[] buf = new byte[256];

            MulticastSocket socket = new MulticastSocket(port);
            InetAddress group = InetAddress.getByName(multiCastAddress);
            socket.joinGroup(group);
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            socket.receive(packet);
            senderAddress = packet.getAddress();
            message = new String(packet.getData(), 0, packet.getLength());

            socket.leaveGroup(group);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
