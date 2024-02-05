package project.bowtie.Model.BTmodel.Nodes;

import java.util.ArrayList;
import java.util.List;

public class path {
    private List<Node> nodes;

    public path() {
        this.nodes = new ArrayList<>();
    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }

    // Method to add all nodes from another NodeVector
    public void addAll(path other) {
        for (int i = 0; i < other.size(); i++) {
            this.addNode(other.getNode(i));
        }
    }

    public Node getNode(int index) {
        return nodes.get(index);
    }

    public int size() {
        return nodes.size();
    }

    // Implement the isValid function based on your specific requirements
    public boolean isValid() {
        // Example check: Ensure all nodes are connected
        for (int i = 0; i < nodes.size() - 1; i++) {
            Node current = nodes.get(i);
            Node next = nodes.get(i + 1);
            if (!current.getAfterNodes().containsValue(next) || !next.getBeforeNodes().containsValue(current)) {
                return false;
            }
        }
        return true;
    }
}
