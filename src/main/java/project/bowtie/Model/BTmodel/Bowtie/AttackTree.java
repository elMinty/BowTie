package project.bowtie.Model.BTmodel.Bowtie;

import javafx.util.Pair;
import project.bowtie.Model.BTmodel.Nodes.Node;
import project.bowtie.Model.BTmodel.Nodes.NodeType;
import project.bowtie.Model.BTmodel.Nodes.path;

import java.util.*;

/**
 * Attack Tree class - provides tree structure for before the Top-Event
 * Used in the Bowtie Class
 *
 * @see Bowtie
 * @see Tree
 */
public class AttackTree extends Tree {
    private HashMap<String, Node> rootNodes = new HashMap<>();
    private List<path> paths = new ArrayList<>();
    private static AttackTree instance;
    private Node TopEvent;

    /**
     * Constructor for the AttackTree class
     *
     * @param rootNode The root node of the tree - type node
     */
    private AttackTree(Node rootNode) {
        super(rootNode);

    }

    /**
     * Setter for the top event of the tree
     *
     * @param topEvent The top event of the tree - type node
     */
    public void setTopEvent(Node topEvent) {
        this.TopEvent = topEvent;
    }

    /**
     * Getter for the top event of the tree - if the tree is null, create a new instance
     *
     * @param rootNode The root node of the tree
     * @return The Attack Tree instance
     */
    public static AttackTree getInstance(Node rootNode) {
        if (instance == null) {
            instance = new AttackTree(rootNode);
        }
        return instance;
    }

    // Specific methods for attack tree

    /**
     * @param root top event of the tree
     * @return the minimum depth of the tree
     */
    public int findMinimumDepth(Node root) {
        if (root == null) return 0;
        if (root.isConsequenceLeaf()) return 1;

        int minDepth = Integer.MAX_VALUE;
        for (Node child : root.getBeforeNodes().values()) {
            minDepth = Math.min(minDepth, findMinimumDepth(child));
        }
        return minDepth + 1;
    }

    /**
     * Find the maximum depth of the tree
     *
     * @param root top event of the tree
     * @return the maximum depth of the tree
     */
    public int findMaximumDepth(Node root) {
        if (root == null) return 0;
        if (root.isConsequenceLeaf()) return 1;

        int maxDepth = 0;
        for (Node child : root.getBeforeNodes().values()) {
            maxDepth = Math.max(maxDepth, findMaximumDepth(child));
        }
        return maxDepth + 1;
    }

    /**
     * Count the number of nodes in the tree
     *
     * @param root The top event of the tree
     * @return The number of nodes in the tree
     */
    public int countNodes(Node root) {
        // Base case: if the current node is null, return 0.
        if (root == null) return 0;

        // Initialize the count for this node.
        int count = 1;  // Count this node

        // Recursively count all nodes in the subtrees.
        for (Node child : root.getBeforeNodes().values()) {
            count += countNodes(child);
        }

        // Return the total count.
        return count;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PATH FUNCTIONS \\
    ////////////////////////////////////////////////////////////////////////////////////////////////////////


    public List<String> generateAllPaths() {
        System.out.println(generatePaths(this.TopEvent));
        return generatePaths(this.TopEvent);
    }

    private List<String> generatePaths(Node node) {
        //return empty List if node is null
        if (node == null) {
            System.out.println("Node is null");
            return new ArrayList<>();
        }

        // new paths list
        List<String> paths = new ArrayList<>();

        // if start point add to paths
        if (node.isRootNode()) {
            paths.add(node.getId());
        }

        // if AND node
        if (node.getType() == NodeType.AND) {
            // new child paths and get all children paths of AND
            // List of each path associated with the child
            Map<Node, List<String>> pathsFromChild = new HashMap<>();
            for (Node child : node.getBeforeNodes().values()) {
                pathsFromChild.put(child, generatePaths(child));
            }
            paths.addAll(generatePathCombinations(pathsFromChild));


        } else {
            // for children of OR
            for (Node child : node.getBeforeNodes().values()) {
                // for paths in the generated paths
                List<String> newPaths = generatePaths(child);
                for (String path : newPaths) {
                    paths.add(node.getId() + "<-" + path);
                }
            }
        }
        return paths;
    }

    public static List<String> generatePathCombinations(Map<Node, List<String>> pathsByNode) {
        List<List<String>> allPaths = new ArrayList<>(pathsByNode.values());
        List<String> combinations = new ArrayList<>();
        combinePathsRecursive(allPaths, 0, "", combinations);
        return combinations;
    }

    private static void combinePathsRecursive(List<List<String>> allPaths, int depth, String current, List<String> combinations) {
        if (depth == allPaths.size()) {
            combinations.add("AND(" + current + ")");
            return;
        }

        for (String path : allPaths.get(depth)) {
            combinePathsRecursive(
                    allPaths,
                    depth + 1,
                    current.isEmpty() ? path : current + ", " + path,
                    combinations
            );
        }
    }
}

