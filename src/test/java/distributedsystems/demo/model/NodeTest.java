package distributedsystems.demo.model;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    void receiveMessage() {
//        try {
            Node node = new Node("Node1", "111,111,111,111");
            Node nextNode = new Node("Node2", "111,111,111,112");
            Node previousNode = new Node("Node3", "111,111,111,110");
//            node.setNextNode(nextNode);
//            node.setPreviousNode(previousNode);
            node.receiveMessage("230.0.0.0", 4446);
//            MessagePublisher multicatPublisher = new MessagePublisher();
//            multicatPublisher.multicast("hallo daar", "230.0.0.0");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}