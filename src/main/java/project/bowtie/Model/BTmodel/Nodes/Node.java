package project.bowtie.Model.BTmodel.Nodes;


import project.bowtie.App.Controllers.ViewPane.Obj.Nodes.NodeLabel;

import java.util.HashMap;
import java.util.Map;

/**
 * Node class
 *
 * This class is used to represent a node in the BowTie model.
 * It contains information about the node, such as its type, name, id, and the nodes that come before and after it.
 * It also contains information about the node's description, score, and labels.
 *
 * @see NodeType
 * @see NodeLabel
 * @see NodeUtils
 *
 */

public class Node {

    private NodeType type; // Type of node
    private String name; // Name of node
    private String id; // ID of node
    private Map<String, Node> beforeNodes; // Maps IDs to NodeFactory leading to this node
    private Map<String, Node> afterNodes;  // Maps IDs to NodeFactory that this node leads to
    private Map<String, Node> mitigationNodes; // NodeFactory that mitigate this node
    private String description; // Description of node
    private String score; // Score of node
    private NodeLabel nameLabel; // Label for the name of the node
    private NodeLabel descriptionLabel; // Label for the description of the node
    private NodeLabel scoreLabel; // Label for the score of the node

    /**
     * Constructor for Node
     * Initialises: Hashmaps for beforeNodes, afterNodes, and mitigationNodes
     * Initialises: description, score, nameLabel, descriptionLabel, and scoreLabel
     * Sets the ID, type, and name of the node
     *
     * @param ID ID of node
     * @param type Type of node @see NodeType
     * @param name Name of node
     */
    public Node(String ID, NodeType type, String name) {
        this.type = type;
        this.name = name;
        this.id = ID;
        this.beforeNodes = new HashMap<>();
        this.afterNodes = new HashMap<>();
        this.mitigationNodes = new HashMap<>();
        this.description = "";
        this.score = "";
        this.nameLabel = null;
        this.descriptionLabel = null;
        this.scoreLabel = null;

    }

    /**
     * Destructor for Node
     * Sets type, name, and ID to null
     * Sets beforeNodes, afterNodes, and mitigationNodes to null
     *
     */
    public void destroy() {
        setType(null);
        setName(null);
        setId(null);
        beforeNodes = null;
        afterNodes = null;
        mitigationNodes = null;
    }


    // Tells if this node has before, after, or mitigation nodes
    public boolean hasMitigationNodes() {
        return !mitigationNodes.isEmpty();
    }

    // Tells if this node is a threat or a vulnerability
    public boolean isAttackRoot() {
        return beforeNodes.isEmpty();
    }

    // Tells if this node is a consequence or a mitigation
    public boolean isConsequenceLeaf() {
        return afterNodes.isEmpty();
    }


    // getters /////////////////////////////////////////////////////////////////////////////////////////////////////////
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

    public String getDescription() {
        return description;
    }

    public String getScore() {
        return score;
    }

    public NodeLabel getScoreLabel() {
        return scoreLabel;
    }

    public NodeLabel getNameLabel() {
        return nameLabel;
    }

    public NodeLabel getDescriptionLabel() {
        return descriptionLabel;
    }

    public Map<String, Node> getMitigationNodes() {
        return mitigationNodes;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setScoreLabel(NodeLabel scoreLabel) {
        this.scoreLabel = scoreLabel;
    }

    public void setNameLabel(NodeLabel nameLabel) {
        this.nameLabel = nameLabel;
    }

    public void setDescriptionLabel(NodeLabel descriptionLabel) {
        this.descriptionLabel = descriptionLabel;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}