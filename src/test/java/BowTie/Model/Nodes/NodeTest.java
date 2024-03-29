package BowTie.Model.Nodes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    void destroy() {
        Node node = new Node("TestId", NodeType.NONE, "TestName");
        node.destroy();

        assertNull(node.getType());
        assertNull(node.getName());
        assertNull(node.getId());
        assertNull(node.getBeforeNodes());
        assertNull(node.getAfterNodes());
        assertNull(node.getMitigationNodes());
    }

    @Test
    void hasMitigationNodes() {
        Node node = new Node("TestId", NodeType.NONE, "TestName");
        Node mitigationNode = new Node("TestId2", NodeType.NONE, "TestName2");
        node.addMitigationNode(mitigationNode);
        assertTrue(node.hasMitigationNodes());
    }

    @Test
    void isAttackRoot() {
        Node node = new Node("TestId", NodeType.NONE, "TestName");
        Node afterNode = new Node("TestId2", NodeType.NONE, "TestName2");
        node.addAfterNode(afterNode);
        assertTrue(node.isAttackRoot());
    }

    @Test
    void isConsequenceLeaf() {
        Node node = new Node("TestId", NodeType.NONE, "TestName");
        Node beforeNode = new Node("TestId2", NodeType.NONE, "TestName2");
        node.addBeforeNode(beforeNode);
        assertTrue(node.isConsequenceLeaf());
    }

    @Test
    void addBeforeNode() {
        Node node = new Node("TestId", NodeType.NONE, "TestName");
        Node beforeNode = new Node("TestId2", NodeType.NONE, "TestName2");
        node.addBeforeNode(beforeNode);
        assertTrue(node.getBeforeNodes().containsKey(beforeNode.getId()));
    }

    @Test
    void addAfterNode() {
        Node node = new Node("TestId", NodeType.NONE, "TestName");
        Node afterNode = new Node("TestId2", NodeType.NONE, "TestName2");
        node.addAfterNode(afterNode);
        assertTrue(node.getAfterNodes().containsKey(afterNode.getId()));
    }

    @Test
    void removeBeforeNode() {
        Node node = new Node("TestId", NodeType.NONE, "TestName");
        Node beforeNode = new Node("TestId2", NodeType.NONE, "TestName2");
        node.addBeforeNode(beforeNode);
        node.removeBeforeNode(beforeNode);
        assertFalse(node.getBeforeNodes().containsKey(beforeNode.getId()));
    }

    @Test
    void removeAfterNode() {
        Node node = new Node("TestId", NodeType.NONE, "TestName");
        Node afterNode = new Node("TestId2", NodeType.NONE, "TestName2");
        node.addAfterNode(afterNode);
        node.removeAfterNode(afterNode);
        assertFalse(node.getAfterNodes().containsKey(afterNode.getId()));
    }

    @Test
    void addMitigationNode() {
        Node node = new Node("TestId", NodeType.NONE, "TestName");
        Node mitigationNode = new Node("TestId2", NodeType.NONE, "TestName2");
        node.addMitigationNode(mitigationNode);
        assertTrue(node.getMitigationNodes().containsKey(mitigationNode.getId()));
    }

    @Test
    void removeMitigationNode() {
        Node node = new Node("TestId", NodeType.NONE, "TestName");
        Node mitigationNode = new Node("TestId2", NodeType.NONE, "TestName2");
        node.addMitigationNode(mitigationNode);
        node.removeMitigationNode(mitigationNode);
        assertFalse(node.getMitigationNodes().containsKey(mitigationNode.getId()));
    }
}