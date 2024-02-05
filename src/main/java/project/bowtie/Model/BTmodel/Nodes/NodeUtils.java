package project.bowtie.Model.BTmodel.Nodes;

import java.util.Map;

public class NodeUtils {

    // connects two nodes together

    public static void connect(Node beforeNode, Node afterNode) {
        beforeNode.addAfterNode(afterNode);
        afterNode.addBeforeNode(beforeNode);
    }


    // disconnects two nodes

    public static void disconnect(Node beforeNode, Node afterNode) {
        beforeNode.removeAfterNode(afterNode);
        afterNode.removeBeforeNode(beforeNode);
    }

    // deletes a node and the connections to it
    public static void delete(Node node) {

        // for all nodes before this node, remove this node from their after nodes
        for (Node beforeNode : node.getBeforeNodes().values()) {
            beforeNode.removeAfterNode(node);
        }

        // for all nodes after this node, remove this node from their before nodes

        for (Node afterNode : node.getAfterNodes().values()) {
            afterNode.removeBeforeNode(node);
        }

        node.destroy();

    }

    // removes node maintaining connections
    public static void remove(Node node) {

        // for all nodes touching this node, connect them to each other
        for (Node beforeNode : node.getBeforeNodes().values()) {
            for (Node afterNode : node.getAfterNodes().values()) {
                connect(beforeNode, afterNode);
            }
        }

        // delete the node
        delete(node);
        node.destroy();
    }

    // adds node buffering connections
    public static void insert(Node node, Map<String, Node> beforeNodes, Map<String, Node> afterNodes) {

        // disconnect all nodes in before with nodes from after
        for (Node beforeNode : beforeNodes.values()) {
            for (Node afterNode : afterNodes.values()) {
                disconnect(beforeNode, afterNode);
            }
        }

        // connect all nodes in before with the new node
        for (Node beforeNode : beforeNodes.values()) {
            connect(beforeNode, node);
        }

        // connect all nodes in after with the new node
        for (Node afterNode : afterNodes.values()) {
            connect(node, afterNode);
        }
    }

    public static void combine(Node node1, Node node2, Node newNode) {

        // connect all nodes in before1 with the new node
        for (Node beforeNode : node1.getBeforeNodes().values()) {
            connect(beforeNode, newNode);
        }

        // connect all nodes in before2 with the new node
        for (Node beforeNode : node2.getBeforeNodes().values()) {
            connect(beforeNode, newNode);
        }

        // connect all nodes in after1 with the new node
        for (Node afterNode : node1.getAfterNodes().values()) {
            connect(newNode, afterNode);
        }

        // connect all nodes in after2 with the new node
        for (Node afterNode : node2.getAfterNodes().values()) {
            connect(newNode, afterNode);
        }

        // delete the old nodes
        delete(node1);
        delete(node2);

        node1.destroy();
        node2.destroy();

    }

}
