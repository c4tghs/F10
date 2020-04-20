package distributedsystems.demo.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Thomas Somers
 * @version 1.0 26/03/2020 9:54
 */
public class Node {
    private String name;
    private String ip;
    private List<File> files;
    private Node nextNode;
    private Node previousNode;


    public Node(String name,String ip) {
        this.name = name;
        this.ip = ip;
        files = new ArrayList();

    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
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
