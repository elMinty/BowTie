package project.bowtie.Model.BTmodel.Nodes;

import java.util.HashMap;
import java.util.Map;
import java.util.EnumSet;

public class NodeUtils {

    // Maps to define valid relationships
    private static final Map<NodeType, EnumSet<NodeType>> validPredecessors = new HashMap<>();
    private static final Map<NodeType, EnumSet<NodeType>> validSuccessors = new HashMap<>();
    private static final Map<NodeType, EnumSet<NodeType>> validMitigator = new HashMap<>();

    // define relationships between nodes

//    Node TYPE	        PRECEDING NODES	                                        SUCEEDING NODES                          Mitigator
//    THREAT	        And, Action	                                            Action, Exposure, None                      None
//    AND	            Action, Vulnerability	                                Action, Threat, Top Event                   None
//    ACTION	        Action, Threat, And, Vulnerability,None	                Action, Threat, And, Exposure               Mitigation
//    VULNERABILITY	    None	                                                And                                         Mitigation
//    MITIGATION	    None	                                                none                                        Counter-Mitigation
//    COUNTER Mit	    None	                                                None                                        None
//    EXPOSURE	        And, Action, Threat	                                    None                                        None

    static {

        validPredecessors.put(NodeType.THREAT, EnumSet.of(NodeType.AND, NodeType.ACTION));
        validPredecessors.put(NodeType.AND, EnumSet.of(NodeType.ACTION, NodeType.VULNERABILITY));
        validPredecessors.put(NodeType.ACTION, EnumSet.of(NodeType.ACTION, NodeType.THREAT, NodeType.AND, NodeType.VULNERABILITY, NodeType.NONE, NodeType.TOP_EVENT));
        validPredecessors.put(NodeType.VULNERABILITY, EnumSet.of(NodeType.NONE));
        validPredecessors.put(NodeType.MITIGATION, EnumSet.of(NodeType.NONE));
        validPredecessors.put(NodeType.COUNTER_MITIGATION, EnumSet.of(NodeType.NONE));
        validPredecessors.put(NodeType.EXPOSURE, EnumSet.of(NodeType.AND, NodeType.ACTION, NodeType.THREAT, NodeType.TOP_EVENT));
        validPredecessors.put(NodeType.TOP_EVENT, EnumSet.of(NodeType.AND, NodeType.ACTION,NodeType.NONE));

        validSuccessors.put(NodeType.THREAT, EnumSet.of(NodeType.ACTION, NodeType.EXPOSURE, NodeType.NONE));
        validSuccessors.put(NodeType.AND, EnumSet.of(NodeType.ACTION, NodeType.THREAT, NodeType.TOP_EVENT));
        validSuccessors.put(NodeType.ACTION, EnumSet.of(NodeType.ACTION, NodeType.THREAT, NodeType.AND, NodeType.EXPOSURE, NodeType.TOP_EVENT));
        validSuccessors.put(NodeType.VULNERABILITY, EnumSet.of(NodeType.AND));
        validSuccessors.put(NodeType.MITIGATION, EnumSet.of(NodeType.NONE));
        validSuccessors.put(NodeType.COUNTER_MITIGATION, EnumSet.of(NodeType.NONE));
        validSuccessors.put(NodeType.EXPOSURE, EnumSet.of(NodeType.NONE));
        validSuccessors.put(NodeType.TOP_EVENT, EnumSet.of(NodeType.NONE,NodeType.ACTION, NodeType.EXPOSURE));


        validMitigator.put(NodeType.ACTION, EnumSet.of(NodeType.MITIGATION));
        validMitigator.put(NodeType.VULNERABILITY, EnumSet.of(NodeType.MITIGATION));
        validMitigator.put(NodeType.MITIGATION, EnumSet.of(NodeType.COUNTER_MITIGATION));
        validMitigator.put(NodeType.THREAT, EnumSet.of(NodeType.NONE));
        validMitigator.put(NodeType.AND, EnumSet.of(NodeType.NONE));
        validMitigator.put(NodeType.EXPOSURE, EnumSet.of(NodeType.NONE));
        validMitigator.put(NodeType.COUNTER_MITIGATION, EnumSet.of(NodeType.NONE));





    }


    public static Boolean connect(Node beforeNode, Node afterNode) {

        if (isValidTypeConnection(beforeNode.getType(), afterNode.getType()) && !areConnected(beforeNode, afterNode)) {
            System.out.println("Connecting " + beforeNode.getName() + " to " + afterNode.getName());
            beforeNode.addAfterNode(afterNode);
            afterNode.addBeforeNode(beforeNode);

            return true;
        }
        return false;
    }

    public static Boolean mitigate(Node node, Node mitigator) {
        if (isValidMitigator(node.getType(), mitigator.getType())) {
            node.addMitigationNode(mitigator);
            return true;
        }
        return false;
    }


    // disconnects two nodes

    public static boolean disconnect(Node beforeNode, Node afterNode) {
        if (areConnected(beforeNode, afterNode)) {
            System.out.println("Disconnecting " + beforeNode.getName() + " from " + afterNode.getName());
            beforeNode.removeAfterNode(afterNode);
            afterNode.removeBeforeNode(beforeNode);
            return true;
        }
        System.out.println("Could not disconnect " + beforeNode.getName() + " from " + afterNode.getName());
        return false;

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


    public static boolean isValidTypeConnection(NodeType beforeNode, NodeType afterNode) {

        boolean beforeTrue = validPredecessors.getOrDefault(afterNode, EnumSet.noneOf(NodeType.class)).contains(beforeNode);
        boolean afterTrue = validSuccessors.getOrDefault(beforeNode, EnumSet.noneOf(NodeType.class)).contains(afterNode);


        return beforeTrue && afterTrue;

    }

    public static boolean isValidMitigator(NodeType node, NodeType mitigator) {
        return validMitigator.getOrDefault(node, EnumSet.noneOf(NodeType.class)).contains(mitigator);
    }

    public static boolean areConnected(Node beforeNode, Node afterNode) {

        // get if before is connected to after
        // get if after is connected to before
        boolean beforeAfter = beforeNode.getAfterNodes().containsValue(afterNode);
        boolean afterBefore = afterNode.getBeforeNodes().containsValue(beforeNode);
        return beforeAfter && afterBefore;
    }

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
