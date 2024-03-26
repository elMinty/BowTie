package project.bowtie.Model.BTmodel.Nodes;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NodeUtilsTest {

    @Test
    void connect() {
        Node beforeNode = new Node("1", NodeType.NONE, "BeforeNode");
        Node afterNode = new Node("2", NodeType.NONE, "AfterNode");

        NodeUtils.connect(beforeNode, afterNode);

        assertTrue(beforeNode.getAfterNodes().containsKey("2"));
        assertTrue(afterNode.getBeforeNodes().containsKey("1"));
    }

    @Test
    void disconnect() {
        Node beforeNode = new Node("1", NodeType.NONE, "BeforeNode");
        Node afterNode = new Node("2", NodeType.NONE, "AfterNode");


        NodeUtils.connect(beforeNode, afterNode);
        NodeUtils.disconnect(beforeNode, afterNode);

        assertFalse(beforeNode.getAfterNodes().containsKey("2"));
        assertFalse(afterNode.getBeforeNodes().containsKey("1"));
    }

    @Test
    void delete() {
        Node beforeNode = new Node("1", NodeType.NONE, "BeforeNode");
        Node afterNode = new Node("2", NodeType.NONE, "AfterNode");
        Node node = new Node("3", NodeType.NONE, "Node");

        NodeUtils.connect(node, afterNode);
        NodeUtils.connect(beforeNode, node);

        NodeUtils.delete(node);

        assertFalse(beforeNode.getAfterNodes().containsKey("3"));
        assertFalse(afterNode.getBeforeNodes().containsKey("3"));
        assertFalse(beforeNode.getAfterNodes().containsKey("2"));
        assertFalse(afterNode.getBeforeNodes().containsKey("1"));
    }

    @Test
    void remove() {
        Node beforeNode = new Node("1", NodeType.NONE, "BeforeNode");
        Node afterNode = new Node("2", NodeType.NONE, "AfterNode");
        Node node = new Node("3", NodeType.NONE, "Node");

        NodeUtils.connect(node, afterNode);
        NodeUtils.connect(beforeNode, node);

        NodeUtils.remove(node);

        assertTrue(beforeNode.getAfterNodes().containsKey("2"));
        assertTrue(afterNode.getBeforeNodes().containsKey("1"));
        assertFalse(beforeNode.getAfterNodes().containsKey("3"));
        assertFalse(afterNode.getBeforeNodes().containsKey("3"));
    }

    @Test
    void insert() {
        Node beforeNode = new Node("1", NodeType.NONE, "BeforeNode");
        Node afterNode = new Node("2", NodeType.NONE, "AfterNode");
        Node node = new Node("3", NodeType.NONE, "Node");

        NodeUtils.connect(beforeNode, afterNode);

        Map<String, Node> beforeNodes = new HashMap<>();
        beforeNodes.put(beforeNode.getId(), beforeNode);
        Map<String, Node> afterNodes = new HashMap<>();
        afterNodes.put(afterNode.getId(), afterNode);

        NodeUtils.insert(node, beforeNodes, afterNodes);

        assertTrue(beforeNode.getAfterNodes().containsKey("3"));
        assertTrue(afterNode.getBeforeNodes().containsKey("3"));
        assertFalse(beforeNode.getAfterNodes().containsKey("2"));
        assertFalse(afterNode.getBeforeNodes().containsKey("1"));
    }

    @Test
    void combine() {
        Node beforeNode = new Node("1", NodeType.NONE, "BeforeNode");
        Node afterNode = new Node("2", NodeType.NONE, "AfterNode");
        Node node1 = new Node("3", NodeType.NONE, "Node1");
        Node node2 = new Node("4", NodeType.NONE, "Node2");
        Node newNode = new Node("5", NodeType.NONE, "NewNode");

        NodeUtils.connect(beforeNode, node1);
        NodeUtils.connect(node1, afterNode);
        NodeUtils.connect(beforeNode, node2);
        NodeUtils.connect(node2, afterNode);

        NodeUtils.combine(node1, node2, newNode);

        assertTrue(beforeNode.getAfterNodes().containsKey("5"));
        assertTrue(afterNode.getBeforeNodes().containsKey("5"));
        assertFalse(beforeNode.getAfterNodes().containsKey("3"));
        assertFalse(afterNode.getBeforeNodes().containsKey("3"));
        assertFalse(beforeNode.getAfterNodes().containsKey("4"));
        assertFalse(afterNode.getBeforeNodes().containsKey("4"));
    }

    @Test
    void isValidConnection() {

        assertTrue(NodeUtils.isValidTypeConnection(NodeType.THREAT, NodeType.ACTION));
        assertTrue(NodeUtils.isValidTypeConnection(NodeType.THREAT, NodeType.EXPOSURE));
        assertTrue(NodeUtils.isValidTypeConnection(NodeType.THREAT, NodeType.NONE));
        assertFalse(NodeUtils.isValidTypeConnection(NodeType.THREAT, NodeType.VULNERABILITY));
        assertFalse(NodeUtils.isValidMitigator(NodeType.THREAT, NodeType.MITIGATION));
        assertFalse(NodeUtils.isValidMitigator(NodeType.THREAT, NodeType.COUNTER_MITIGATION));

        assertTrue(NodeUtils.isValidTypeConnection(NodeType.AND, NodeType.ACTION));
        assertTrue(NodeUtils.isValidTypeConnection(NodeType.AND, NodeType.THREAT));
        assertFalse(NodeUtils.isValidTypeConnection(NodeType.AND, NodeType.EXPOSURE));
        assertFalse(NodeUtils.isValidTypeConnection(NodeType.AND, NodeType.NONE));
        assertTrue(NodeUtils.isValidTypeConnection(NodeType.AND, NodeType.VULNERABILITY));
        assertFalse(NodeUtils.isValidMitigator(NodeType.AND, NodeType.MITIGATION));
        assertFalse(NodeUtils.isValidMitigator(NodeType.AND, NodeType.COUNTER_MITIGATION));

        assertTrue(NodeUtils.isValidTypeConnection(NodeType.ACTION, NodeType.ACTION));
        assertTrue(NodeUtils.isValidTypeConnection(NodeType.ACTION, NodeType.THREAT));
        assertTrue(NodeUtils.isValidTypeConnection(NodeType.ACTION, NodeType.AND));
        assertTrue(NodeUtils.isValidTypeConnection(NodeType.ACTION, NodeType.VULNERABILITY));
        assertTrue(NodeUtils.isValidTypeConnection(NodeType.ACTION, NodeType.MITIGATION));
        assertTrue(NodeUtils.isValidTypeConnection(NodeType.ACTION, NodeType.NONE));
        assertFalse(NodeUtils.isValidMitigator(NodeType.ACTION, NodeType.EXPOSURE));
        assertFalse(NodeUtils.isValidMitigator(NodeType.ACTION, NodeType.COUNTER_MITIGATION));

        assertFalse(NodeUtils.isValidTypeConnection(NodeType.VULNERABILITY, NodeType.ACTION));
        assertFalse(NodeUtils.isValidTypeConnection(NodeType.VULNERABILITY, NodeType.THREAT));
        assertFalse(NodeUtils.isValidTypeConnection(NodeType.VULNERABILITY, NodeType.AND));
        assertFalse(NodeUtils.isValidTypeConnection(NodeType.VULNERABILITY, NodeType.VULNERABILITY));
        assertFalse(NodeUtils.isValidTypeConnection(NodeType.VULNERABILITY, NodeType.MITIGATION));
        assertFalse(NodeUtils.isValidTypeConnection(NodeType.VULNERABILITY, NodeType.NONE));
        assertTrue(NodeUtils.isValidMitigator(NodeType.VULNERABILITY, NodeType.EXPOSURE));
        assertFalse(NodeUtils.isValidMitigator(NodeType.VULNERABILITY, NodeType.COUNTER_MITIGATION));

        assertTrue(NodeUtils.isValidTypeConnection(NodeType.MITIGATION, NodeType.ACTION));
        assertTrue(NodeUtils.isValidTypeConnection(NodeType.MITIGATION, NodeType.VULNERABILITY));
    }


}