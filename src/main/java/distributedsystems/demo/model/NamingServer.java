package distributedsystems.demo.model;

import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public NamingServer() {
        nodes = new HashMap();
        xmlParser = new XMLParser();
        multicastReceiver = new MulticastReceiver();
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

                diffHashcode = fileHashcode - entry.getKey();
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

    public void listenToMulticast() {
        multicastReceiver.run();
    }
// TODO: controle op al aanwezige bestanden
    public int replicateFiles(int fileHash) {
        int hash =0;
        HashMap<Integer, Integer> smallerNodes = new HashMap<>();
        HashMap<Integer, Integer> biggerNodes = new HashMap<>();
        for (Map.Entry<Integer, String> node : nodes.entrySet()) {
            if (node.getKey() < fileHash) {
                int diff = fileHash - node.getKey();
                smallerNodes.put(node.getKey(), diff);
                return node.getKey();
            } else if (node.getKey() > fileHash) {
                int diff = node.getKey() - fileHash;
                biggerNodes.put(node.getKey(), diff);
            }
        }
        if (smallerNodes.size() == 0) {
            int biggerdiff = 0;
            for (Map.Entry<Integer, Integer> node : biggerNodes.entrySet()) {
                if (node.getValue() > biggerdiff) {
                    biggerdiff = node.getValue();
                    hash = node.getKey();
                }
            }
            } else {
            int initdiff = 1000000;
            for (Map.Entry<Integer, Integer> node : smallerNodes.entrySet()) {
                if (node.getValue() < initdiff) {
                    hash = node.getKey();
                    initdiff = node.getValue();
                }
            }
        }
        return hash;
    }
}