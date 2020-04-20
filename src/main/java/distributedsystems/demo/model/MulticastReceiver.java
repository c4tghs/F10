package distributedsystems.demo.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];

    public void multiReceiver(String multiCastAddress, int port) {
        try {
            socket = new MulticastSocket(port);
            InetAddress group = InetAddress.getByName(multiCastAddress);
            socket.joinGroup(group);
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                System.out.write(packet.getData(),0,packet.getLength());
                String received = new String(packet.getData(), 0, packet.getLength());
                if ("end".equals(received)) break;
            }
            socket.leaveGroup(group);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
