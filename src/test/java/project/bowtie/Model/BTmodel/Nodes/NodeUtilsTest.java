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
        //    Node TYPE	        PRECEDING NODES	                                        SUCEEDING NODES
//    THREAT	        And, Action	                                            Action, Exposure, None
//    AND	            Action, Vulnerability	                                Action, Threat
//    ACTION	        Action, Threat, And, Vulnerability, Mitigation, None	Action, Threat, And, Exposure
//    VULNERABILITY	    None	                                                And
//    MITIGATION	    Counter-Mitigation , None	                            Action, Vulnerability
//    COUNTER Mit	    None	                                                Mitigation
//    EXPOSURE	        And, Action, Threat	                                    None

        assertTrue(NodeUtils.isValidConnection(NodeType.THREAT, NodeType.ACTION));
        assertTrue(NodeUtils.isValidConnection(NodeType.THREAT, NodeType.EXPOSURE));
        assertTrue(NodeUtils.isValidConnection(NodeType.THREAT, NodeType.NONE));
        assertFalse(NodeUtils.isValidConnection(NodeType.THREAT, NodeType.VULNERABILITY));
        assertFalse(NodeUtils.isValidConnection(NodeType.THREAT, NodeType.MITIGATION));
        assertFalse(NodeUtils.isValidConnection(NodeType.THREAT, NodeType.COUNTER_MITIGATION));

        assertTrue(NodeUtils.isValidConnection(NodeType.AND, NodeType.ACTION));
        assertTrue(NodeUtils.isValidConnection(NodeType.AND, NodeType.THREAT));
        assertFalse(NodeUtils.isValidConnection(NodeType.AND, NodeType.EXPOSURE));
        assertFalse(NodeUtils.isValidConnection(NodeType.AND, NodeType.NONE));
        assertTrue(NodeUtils.isValidConnection(NodeType.AND, NodeType.VULNERABILITY));
        assertFalse(NodeUtils.isValidConnection(NodeType.AND, NodeType.MITIGATION));
        assertFalse(NodeUtils.isValidConnection(NodeType.AND, NodeType.COUNTER_MITIGATION));

        assertTrue(NodeUtils.isValidConnection(NodeType.ACTION, NodeType.ACTION));
        assertTrue(NodeUtils.isValidConnection(NodeType.ACTION, NodeType.THREAT));
        assertTrue(NodeUtils.isValidConnection(NodeType.ACTION, NodeType.AND));
        assertTrue(NodeUtils.isValidConnection(NodeType.ACTION, NodeType.VULNERABILITY));
        assertTrue(NodeUtils.isValidConnection(NodeType.ACTION, NodeType.MITIGATION));
        assertTrue(NodeUtils.isValidConnection(NodeType.ACTION, NodeType.NONE));
        assertFalse(NodeUtils.isValidConnection(NodeType.ACTION, NodeType.EXPOSURE));
        assertFalse(NodeUtils.isValidConnection(NodeType.ACTION, NodeType.COUNTER_MITIGATION));

        assertFalse(NodeUtils.isValidConnection(NodeType.VULNERABILITY, NodeType.ACTION));
        assertFalse(NodeUtils.isValidConnection(NodeType.VULNERABILITY, NodeType.THREAT));
        assertFalse(NodeUtils.isValidConnection(NodeType.VULNERABILITY, NodeType.AND));
        assertFalse(NodeUtils.isValidConnection(NodeType.VULNERABILITY, NodeType.VULNERABILITY));
        assertFalse(NodeUtils.isValidConnection(NodeType.VULNERABILITY, NodeType.MITIGATION));
        assertFalse(NodeUtils.isValidConnection(NodeType.VULNERABILITY, NodeType.NONE));
        assertTrue(NodeUtils.isValidConnection(NodeType.VULNERABILITY, NodeType.EXPOSURE));
        assertFalse(NodeUtils.isValidConnection(NodeType.VULNERABILITY, NodeType.COUNTER_MITIGATION));

        assertTrue(NodeUtils.isValidConnection(NodeType.MITIGATION, NodeType.ACTION));
        assertTrue(NodeUtils.isValidConnection(NodeType.MITIGATION, NodeType.VULNERABILITY));
    }


}