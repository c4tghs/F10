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

    private int nextId;
    private int prevId;
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
        if (id < hash && hash < nextId) {
            nextId = hash;
            try {
                messagePublisher.unicast(String.valueOf(hash), String.valueOf(inetAddress));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (prevId < hash && hash < id) {
            prevId = hash;
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

    public void discover(String address) throws IOException {
        String message = String.format("Hey, I am %s with ip: %s", name, ip);
        messagePublisher.multicast(message, address);
    }
    public void shutdown(String address) throws IOException {
        String nid = String.valueOf(nextId);
        
        messagePublisher.unicast(nid, "");
    }
    public int getNextId() {
        return nextId;
    }

    public int getPrevId() {
        return prevId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

    public void setPrevId(int prevId) {
        this.prevId = prevId;
    }
}
