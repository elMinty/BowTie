package project.bowtie.Model.BTmodel.Nodes;

import java.util.HashMap;
import java.util.Map;

public class Node {
    private NodeType type; // Type of node
    private String name; // Name of node
    private String id; // ID of node
    private Map<String, Node> beforeNodes; // Maps IDs to NodeFactory leading to this node
    private Map<String, Node> afterNodes;  // Maps IDs to NodeFactory that this node leads to
    private Map<String, Node> mitigationNodes; // NodeFactory that mitigate this node

    public Node(String ID, NodeType type, String name) {
        this.type = type;
        this.name = name;
        this.id = ID;
        this.beforeNodes = new HashMap<>();
        this.afterNodes = new HashMap<>();
        this.mitigationNodes = new HashMap<>();

    }

    // destructor
    public void destroy() {
        setType(null);
        setName(null);
        setId(null);
        beforeNodes = null;
        afterNodes = null;
        mitigationNodes = null;

    }



    // returns if this node had any mitigation nodes

    public boolean hasMitigationNodes() {
        return !mitigationNodes.isEmpty();
    }

    // Tells if this node is a threat or a vulnerability

    public boolean isAttackRoot() {
        return beforeNodes.isEmpty();
    }

    public boolean isConsequenceLeaf() {
        return afterNodes.isEmpty();
    }

    // getters

    public NodeType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Map<String, Node> getAfterNodes() {
        return afterNodes;
    }

    public Map<String, Node> getBeforeNodes() {
        return beforeNodes;
    }

    // setters

    public void setType(NodeType type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Logic for adding and removing nodes

    public void addBeforeNode(Node node) {
        beforeNodes.put(node.getId(), node);
    }

    public void addAfterNode(Node node) {
        afterNodes.put(node.getId(), node);
    }

    public void removeBeforeNode(Node node) {
        beforeNodes.remove(node.getId());
    }

    public void removeAfterNode(Node node) {
        afterNodes.remove(node.getId());
    }

    public void addMitigationNode(Node node) {
        mitigationNodes.put(node.getId(), node);
    }

    public void removeMitigationNode(Node node) {
        mitigationNodes.remove(node.getId());
    }

    public Map<String, Node> getMitigationNodes() {
        return mitigationNodes;
    }

    // print and testing

    public void listAdjoiningNodes() {
        // create a string list of the node ids and types that become before and after nodes
        // in format before: {(id, type), (id, type), ...}

        StringBuilder before = new StringBuilder();
        StringBuilder after = new StringBuilder();

        for (Node node : beforeNodes.values()) {
            before.append("(").append(node.getId()).append(", ").append(node.getType()).append("), ");
        }

        for (Node node : afterNodes.values()) {
            after.append("(").append(node.getId()).append(", ").append(node.getType()).append("), ");
        }

        System.out.println("Before: {" + before + "}");

        System.out.println("After: {" + after + "}");





    }
}