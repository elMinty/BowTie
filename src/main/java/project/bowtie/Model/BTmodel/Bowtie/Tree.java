package project.bowtie.Model.BTmodel.Bowtie;

import project.bowtie.Model.BTmodel.Nodes.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is the abstract class for the attack tree and consequence tree
 * Provides Inherent functions and node counter
 *
 * @author Euan Murray
 * @see AttackTree
 * @see ConsequenceTree
 */
public abstract class Tree {
    protected Node TopEvent;
    protected Map<String, Node> nodes;
    protected int numberOfNodes;
    protected int MaxDepth;
    protected int minDepth;
    private HashMap<String, Node> leafNodes;
    private List<path> paths;

    /**
     * Constructor for the Tree class
     *
     * @param topEvent The top event of the tree - type node
     */
    public Tree(Node topEvent) {
        this.TopEvent = topEvent;
        this.nodes = new HashMap<>();
        this.nodes.put(topEvent.getId(), topEvent);
        this.numberOfNodes = 1;
        this.MaxDepth = 0;
        this.minDepth = 0;
        this.leafNodes = new HashMap<>();
        this.paths = new ArrayList<>();
    }

    // Common methods for both attack tree and consequence tree

    // counter functions
    public void plusNodes(int node_number, Map<String, Node> nodes){
        this.numberOfNodes += node_number;
        this.nodes.putAll(nodes);
    }

    public void minusNode(int numberOfNodes, Map<String, Node> nodes){
        this.numberOfNodes -= numberOfNodes;
        for (Node node : nodes.values()) {
            this.nodes.remove(node.getId());
        }
    }

}

