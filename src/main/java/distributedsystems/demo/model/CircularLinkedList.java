package distributedsystems.demo.model;

/**
 * @author Thomas Somers
 * @version 1.0 30/03/2020 9:09
 */
public class CircularLinkedList {
    private Node head = null; //eerste node
    private Node tail = null; //laatste node

    public void addNode(Node node) {
        if (head == null) {
            head = node;
        } else {
            tail.setNextNode(node);
        }

        tail = node;
        tail.setNextNode(head);
    }
}
