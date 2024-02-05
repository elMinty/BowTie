package project.bowtie.Model.BTmodel.Bowtie;

import project.bowtie.Model.BTmodel.Nodes.Node;
import project.bowtie.Model.BTmodel.Nodes.path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AttackTree extends Tree {
    private HashMap<String, Node> rootNodes = new HashMap<>();
    private List<path> paths = new ArrayList<>();
    private static AttackTree instance;

    private AttackTree(Node rootNode) {
        super(rootNode);
    }

    public static AttackTree getInstance(Node rootNode) {
        if (instance == null) {
            instance = new AttackTree(rootNode);
        }
        return instance;
    }

    // Specific methods for attack tree
    // counter functions
    public int findMinimumDepth(Node root) {
        if (root == null) return 0;
        if (root.isConsequenceLeaf()) return 1;

        int minDepth = Integer.MAX_VALUE;
        for (Node child : root.getBeforeNodes().values()) {
            minDepth = Math.min(minDepth, findMinimumDepth(child));
        }
        return minDepth + 1;
    }

    public int findMaximumDepth(Node root) {
        if (root == null) return 0;
        if (root.isConsequenceLeaf()) return 1;

        int maxDepth = 0;
        for (Node child : root.getBeforeNodes().values()) {
            maxDepth = Math.max(maxDepth, findMaximumDepth(child));
        }
        return maxDepth + 1;
    }

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

    /*public void generateAttackVectors() {
        this.paths.clear(); // Clear existing paths
        findAllPaths(this.TopEvent, new path(), this.paths);

        // Sort the paths by size (from smallest to largest)
        this.paths.sort(Comparator.comparingInt(path::size));
    }

    private void findAllPaths(Node current, path currentPath, List<path> paths) {
        currentPath.addNode(current);

        // Check if the current node is one of the attack roots
        if (rootNodes.containsValue(current)) {
            path newPath = new path(); // Create a new path
            newPath.addAll(currentPath); // Add all nodes from currentPath to newPath
            paths.add(newPath); // Add a copy of the currentPath to the list of paths
            return;
        }

        // Recursively search for paths in each connected 'before' node
        for (Node previousNode : current.getBeforeNodes().values()) {
            path newPath = new path(); // Create a new path
            newPath.addAll(currentPath); // Add all nodes from currentPath to newPath
            findAllPaths(previousNode, newPath, paths);
        }
    }*/
}
