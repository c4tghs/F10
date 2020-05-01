package distributedsystems.demo.model;

import lombok.Data;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Thomas Somers
 * @version 1.0 26/03/2020 9:54
 */

@Data
public class NamingServer {
    private HashMap<Integer, String> nodes;
    private XMLParser xmlParser;

    private MulticastReceiver multicastReceiver;
    private MessagePublisher messagePublisher;

    public NamingServer() {
        nodes = new HashMap();
        xmlParser = new XMLParser();
        multicastReceiver = new MulticastReceiver();
        messagePublisher = new MessagePublisher();
    }

    private int generateHash(String name) {
        return name.hashCode() % 32768;
    }

    public void addNode(Node node) {
        nodes.put(generateHash(node.getName()), node.getIp());
        xmlParser.addToXML(node);
    }

    public void removeNode(Node node) {
        nodes.remove(generateHash(node.getName()));
        //TODO : xmlParser remove node
    }

    public String findFile(String filename) {
        int fileHashcode = generateHash(filename);
        int diffHashcode;
        int tmpId = 0;
        int tmpHashcode = Integer.MAX_VALUE;

        for (Map.Entry<Integer, String> entry : nodes.entrySet()) {

            if (entry.getKey() < fileHashcode) {

                diffHashcode = fileHashcode- entry.getKey();
                if (diffHashcode < tmpHashcode) {
                    tmpHashcode = diffHashcode;
                    tmpId = entry.getKey();
                }
            }
        }

        return nodes.get(tmpId);
    }

    public int getSize() {
        return nodes.size();
    }

    public void listenToMulticast( String multicastAddress, int port) {
        multicastReceiver.setPort(port);
        multicastReceiver.setMultiCastAddress(multicastAddress);

        multicastReceiver.run();

        String message = multicastReceiver.getMessage(); //Message = name
        InetAddress senderAddress = multicastReceiver.getSenderAddress();

        int hash = generateHash(message);
        nodes.put(hash, senderAddress.toString());

        try {
            messagePublisher.unicast(String.valueOf(nodes.size()),senderAddress.toString(), 1000);
        } catch (IOException e) {
            System.out.println("Failed to send response!");
        }


    }
}
