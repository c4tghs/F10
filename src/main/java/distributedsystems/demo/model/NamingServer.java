package distributedsystems.demo.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Thomas Somers
 * @version 1.0 26/03/2020 9:54
 */
public class NamingServer {
    private HashMap<Integer, String> nodes;

    public NamingServer() {
        nodes = new HashMap();
    }

    public void addNode(Node node) {
        if (getSize() < 1) {
            node.setPrevId(node.getId());
            node.setNextId(node.getId());
        }
        nodes.put(generateHash(node.getName()), node.getIp());
    }

    public void removeNode(int id) {

        nodes.remove(id);
    }

    private int generateHash(String name) {
        int hashcode = name.hashCode() % 32768;

        return hashcode;

    }
    public HashMap getNodes()
    {
        return nodes;
    }
    public void printAllnodes() {
        for (Map.Entry<Integer, String> entry : nodes.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

    public String findFile(String filename) {
        int fileHashcode = generateHash(filename);
        int diffHashcode;
        int tmpId = 0;
        int tmpHashcode = 1000000;

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
}
