package distributedsystems.demo.model;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Thomas Somers
 * @version 1.0 26/03/2020 9:54
 */
public class Node {
    private String name;
    private String ip;
    private int id;
    private List<File> files;
    private Node nextNode;
    private Node previousNode;
    private MulticastReceiver multicastReceiver;
    private MessagePublisher messagePublisher;

    public Node(String name,String ip) {
        this.name = name;
        this.ip = ip;
        id = generateHash(name);
        files = new ArrayList();
        multicastReceiver = new MulticastReceiver();
        messagePublisher = new MessagePublisher();
    }
    private int generateHash(String name) {
        int hashcode = name.hashCode() % 32768;
        return hashcode;
    }
    public void receiveMessage(String multicastAddress, int port) {
        multicastReceiver.multiReceiver(multicastAddress, port);
        String receivedName = multicastReceiver.getReceived();
        InetAddress inetAddress = multicastReceiver.getInetAddress();
        int hash = generateHash(receivedName);
        if (id < hash && hash < nextNode.getId()) {
//            int nextId = hash;
            try {
                messagePublisher.unicast(String.valueOf(hash), String.valueOf(inetAddress));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (previousNode.getId() < hash && hash < id) {
//            int prevId = hash;
            try {
                messagePublisher.unicast(String.valueOf(hash), String.valueOf(inetAddress));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addFile(File file) {
        files.add(file);
    }

    public List<File> getFiles() {
        return files;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }
}
