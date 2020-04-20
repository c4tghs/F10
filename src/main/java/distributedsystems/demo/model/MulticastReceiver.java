package distributedsystems.demo.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];
    private String received;
    private InetAddress inetAddress;

    public void multiReceiver(String multiCastAddress, int port) {
        try {
            socket = new MulticastSocket(port);
            InetAddress group = InetAddress.getByName(multiCastAddress);
            socket.joinGroup(group);
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
//            while (packet.getData() == null) {
                socket.receive(packet);
                inetAddress = packet.getAddress();
                received = new String(packet.getData(), 0, packet.getLength());
//            }
            socket.leaveGroup(group);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getReceived() {
        return received;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }
}
