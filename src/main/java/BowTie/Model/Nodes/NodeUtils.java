package BowTie.Model.Nodes;

import java.util.HashMap;
import java.util.Map;
import java.util.EnumSet;


/**
 * NodeUtils class
 * -----------------
 * This class is used to define the relationships between nodes in the BowTie model.
 * It contains methods to connect, disconnect, delete, remove, insert, and combine nodes.
 * It also contains methods to check if a connection is valid and if a mitigator is valid.
 * -----------------
 *
 */
public class NodeUtils {

    // Maps to define valid relationships
    private static final Map<NodeType, EnumSet<NodeType>> validPredecessors = new HashMap<>();
    private static final Map<NodeType, EnumSet<NodeType>> validSuccessors = new HashMap<>();
    private static final Map<NodeType, EnumSet<NodeType>> validMitigator = new HashMap<>();

    // define relationships between nodes

    /*
     * Static block to define valid relationships between nodes
     * -----------------
     * NodeType: THREAT
     * Preceding Nodes: AND, ACTION
     * Succeeding Nodes: ACTION, EXPOSURE, AND, NONE
     * Mitigator: NONE
     * -----------------
     * NodeType: AND
     * Preceding Nodes: ACTION, VULNERABILITY, AND, THREAT, TOP-EVENT
     * Succeeding Nodes: ACTION, THREAT, TOP EVENT, EXPOSURE
     * Mitigator: NONE
     * -----------------
     * NodeType: ACTION
     * Preceding Nodes: ACTION, THREAT, AND, VULNERABILITY, NONE
     * Succeeding Nodes: ACTION, THREAT, AND, EXPOSURE, TOP EVENT
     * Mitigator: Mitigation
     * -----------------
     * NodeType: VULNERABILITY
     * Preceding Nodes: NONE
     * Succeeding Nodes: AND, ACTION
     * Mitigator:None
     * -----------------
     * NodeType: MITIGATION
     * Preceding Nodes: NONE
     * Succeeding Nodes: NONE
     * Mitigator: Counter-Mitigation
     * -----------------
     * NodeType: COUNTER Mit
     * Preceding Nodes: NONE
     * Succeeding Nodes: NONE
     * Mitigator: NONE
     * -----------------
     * NodeType: EXPOSURE
     * Preceding Nodes: AND, ACTION, THREAT, TOP EVENT, EXPOSURE
     * Succeeding Nodes: EXPOSURE, ACTION, AND
     * Mitigator: NONE
     * -----------------
     * NodeType: TOP EVENT
     * Preceding Nodes: AND, ACTION
     * Succeeding Nodes: ACTION, EXPOSURE, AND
     * Mitigator: NONE
     *
     */
    static {
        // define valid relationships between nodes
        validPredecessors.put(NodeType.THREAT, EnumSet.of(NodeType.AND, NodeType.ACTION));
        validSuccessors.put(NodeType.THREAT, EnumSet.of(NodeType.ACTION, NodeType.EXPOSURE, NodeType.NONE, NodeType.AND));
        validMitigator.put(NodeType.THREAT, EnumSet.of(NodeType.NONE));

        validPredecessors.put(NodeType.AND, EnumSet.of(NodeType.ACTION, NodeType.VULNERABILITY, NodeType.AND, NodeType.THREAT, NodeType.TOP_EVENT));
        validSuccessors.put(NodeType.AND, EnumSet.of(NodeType.ACTION, NodeType.THREAT, NodeType.TOP_EVENT, NodeType.EXPOSURE));
        validMitigator.put(NodeType.AND, EnumSet.of(NodeType.NONE));

        validPredecessors.put(NodeType.ACTION, EnumSet.of(NodeType.ACTION, NodeType.THREAT, NodeType.AND, NodeType.VULNERABILITY, NodeType.NONE));
        validSuccessors.put(NodeType.ACTION, EnumSet.of(NodeType.ACTION, NodeType.THREAT, NodeType.AND, NodeType.EXPOSURE, NodeType.TOP_EVENT));
        validMitigator.put(NodeType.ACTION, EnumSet.of(NodeType.MITIGATION));

        validPredecessors.put(NodeType.VULNERABILITY, EnumSet.of(NodeType.NONE));
        validSuccessors.put(NodeType.VULNERABILITY, EnumSet.of(NodeType.AND, NodeType.ACTION));
        validMitigator.put(NodeType.VULNERABILITY, EnumSet.of(NodeType.NONE));

        validPredecessors.put(NodeType.MITIGATION, EnumSet.of(NodeType.NONE));
        validSuccessors.put(NodeType.MITIGATION, EnumSet.of(NodeType.NONE));
        validMitigator.put(NodeType.MITIGATION, EnumSet.of(NodeType.COUNTER_MITIGATION));

        validPredecessors.put(NodeType.COUNTER_MITIGATION, EnumSet.of(NodeType.NONE));
        validSuccessors.put(NodeType.COUNTER_MITIGATION, EnumSet.of(NodeType.NONE));
        validMitigator.put(NodeType.COUNTER_MITIGATION, EnumSet.of(NodeType.NONE));

        validPredecessors.put(NodeType.EXPOSURE, EnumSet.of(NodeType.AND, NodeType.ACTION, NodeType.THREAT, NodeType.TOP_EVENT, NodeType.EXPOSURE));
        validSuccessors.put(NodeType.EXPOSURE, EnumSet.of(NodeType.EXPOSURE, NodeType.ACTION, NodeType.AND));
        validMitigator.put(NodeType.EXPOSURE, EnumSet.of(NodeType.NONE));

        validPredecessors.put(NodeType.TOP_EVENT, EnumSet.of(NodeType.AND, NodeType.ACTION));
        validSuccessors.put(NodeType.TOP_EVENT, EnumSet.of(NodeType.ACTION, NodeType.EXPOSURE, NodeType.AND));
        validMitigator.put(NodeType.TOP_EVENT, EnumSet.of(NodeType.NONE));



    }

    /**
     * Connects two nodes
     *
     * @param beforeNode The node before the connection
     * @param afterNode The node after the connection
     * @return true if the connection is successful, false otherwise
     */
    public static Boolean connect(Node beforeNode, Node afterNode) {

        if (isValidTypeConnection(beforeNode.getType(), afterNode.getType()) && !areConnected(beforeNode, afterNode)) {
            beforeNode.addAfterNode(afterNode);
            afterNode.addBeforeNode(beforeNode);

            return true;
        }
        return false;
    }

    /**
     * Add a mitigator to a node
     *
     * @param node The node to be mitigated
     * @param mitigator The node that mitigates the node
     * @return true if the mitigation is successful, false otherwise
     */
    public static Boolean mitigate(Node node, Node mitigator) {
        if (isValidMitigator(node.getType(), mitigator.getType())) {
            node.addMitigationNode(mitigator);
            return true;
        }
        return false;
    }

    /**
     * Disconnects two nodes
     *
     * @param beforeNode The node before the connection
     * @param afterNode The node after the connection
     * @return true if the disconnection is successful, false otherwise
     */
    public static boolean disconnect(Node beforeNode, Node afterNode) {
        if (areConnected(beforeNode, afterNode)) {
            beforeNode.removeAfterNode(afterNode);
            afterNode.removeBeforeNode(beforeNode);
            return true;
        }
        return false;

    }

    /**
     * Deletes a node - manages all connections
     *
     * @param node The node to be deleted
     */
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

    /**
     * Removes a node - manages all connections
     * Instead of simply deleting the node, this method connects all nodes before and after the node
     *
     * @param node The node to be removed
     */
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

    /**
     * Inserts a new node between two sets of nodes
     *
     * @param node The new node to be inserted
     * @param beforeNodes The nodes before the new node
     * @param afterNodes The nodes after the new node
     */
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

    /**
     * Combines two nodes into a new node
     *
     * @param node1 The first node to be combined
     * @param node2 The second node to be combined
     * @param newNode The new node to be created
     */
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

    }

    /**
     * Checks if a connection between two nodes is valid - Via the static maps
     *
     * @param beforeNode The node before the connection
     * @param afterNode The node after the connection
     * @return true if the connection is valid, false otherwise
     */
    public static boolean isValidTypeConnection(NodeType beforeNode, NodeType afterNode) {

        boolean beforeTrue = validPredecessors.getOrDefault(afterNode, EnumSet.noneOf(NodeType.class)).contains(beforeNode);
        boolean afterTrue = validSuccessors.getOrDefault(beforeNode, EnumSet.noneOf(NodeType.class)).contains(afterNode);


        return beforeTrue && afterTrue;

    }

    /** Checks if a mitigator is valid for a given node
     *
     * @param node The node to be mitigated
     * @param mitigator The node that mitigates the node
     * @return true if the mitigator is valid, false otherwise
     */
    public static boolean isValidMitigator(NodeType node, NodeType mitigator) {
        return validMitigator.getOrDefault(node, EnumSet.noneOf(NodeType.class)).contains(mitigator);
    }

    /**
     * Checks if two nodes are connected
     *
     * @param beforeNode The node before the connection
     * @param afterNode The node after the connection
     * @return true if the nodes are connected, false otherwise
     */
    public static boolean areConnected(Node beforeNode, Node afterNode) {

        // get if before is connected to after
        // get if after is connected to before
        boolean beforeAfter = beforeNode.getAfterNodes().containsValue(afterNode);
        boolean afterBefore = afterNode.getBeforeNodes().containsValue(beforeNode);
        return beforeAfter && afterBefore;
    }

    /**
     * Gets the NodeType from a string
     *
     * @param type The string representation of the NodeType
     * @return The NodeType
     */
    public static NodeType getNodeType(String type) {
        switch (type) {
            case "THREAT":
                return NodeType.THREAT;
            case "AND":
                return NodeType.AND;
            case "ACTION":
                return NodeType.ACTION;
            case "VULNERABILITY":
                return NodeType.VULNERABILITY;
            case "MITIGATION":
                return NodeType.MITIGATION;
            case "COUNTER_MITIGATION":
                return NodeType.COUNTER_MITIGATION;
            case "EXPOSURE":
                return NodeType.EXPOSURE;
            case "TOP_EVENT":
                return NodeType.TOP_EVENT;
            default:
                return NodeType.NONE;
        }
    }

}
