package project.bowtie.Model.BTmodel.Bowtie;

import project.bowtie.Model.BTmodel.Nodes.*;

/**
 * Consequence Tree class - provides tree structure for after the Top-Event
 * Used in the Bowtie Class
 *
 * @see Bowtie
 * @see Tree
 */
public class ConsequenceTree extends Tree {
    private static ConsequenceTree instance;

    /**
     * Constructor for the ConsequenceTree class
     *
     * @param rootNode The root node of the tree - type node
     */
    public ConsequenceTree(Node rootNode) {
        super(rootNode);

    }

    /**
     * Setter for the top event of the tree
     * @param topEvent The top event of the tree - type node
     */
    public void setTopEvent(Node topEvent) {
        this.TopEvent = topEvent;
    }

    /**
     * Getter for the top event of the tree - if the tree is null, create a new instance
     * @param rootNode The root node of the tree
     * @return The Consequence Tree instance
     */
    public static ConsequenceTree getInstance(Node rootNode) {
        if (instance == null) {
            instance = new ConsequenceTree(rootNode);
        }
        return instance;
    }

    /**
     *
     * @param root top event of the tree
     * @return the minimum depth of the tree
     */
    public static int findMinimumDepth(Node root) {
        if (root == null) return 0;
        if (root.isConsequenceLeaf()) return 1;

        int minDepth = Integer.MAX_VALUE;
        for (Node child : root.getAfterNodes().values()) {
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
    public static int findMaximumDepth(Node root) {
        if (root == null) return 0;
        if (root.isConsequenceLeaf()) return 1;

        int maxDepth = 0;
        for (Node child : root.getAfterNodes().values()) {
            maxDepth = Math.max(maxDepth, findMaximumDepth(child));
        }
        return maxDepth + 1;
    }

    /**
     * Count the number of nodes in the tree
     *
     * @param root top event of the tree
     * @return the number of nodes in the tree
     */
    public static int countNodes(Node root) {
        // Base case: if the current node is null, return 0.
        if (root == null) return 0;

        // Initialize the count for this node.
        int count = 1;  // Count this node

        // Recursively count all nodes in the subtrees.
        for (Node child : root.getAfterNodes().values()) {
            count += countNodes(child);
        }

        // Return the total count.
        return count;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // Depreceated Paths functions
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    /*public void generateConsequencePaths() {
        List<path> paths = new ArrayList<>(); // List to store paths
        for (Node leafNode : leafNodes.values()) {
            findAllPaths(leafNode, new path(), paths);
        }

        // Sort the paths by size (from smallest to largest)
        paths.sort((p1, p2) -> Integer.compare(p1.size(), p2.size()));
    }

    private void findAllPaths(Node current, path currentPath, List<path> paths) {
        currentPath.addNode(current);

        // Check if the current node is the top event (root node)
        if (current.equals(this.TopEvent)) {
            paths.add(new path(currentPath)); // Add a copy of the currentPath to the list of paths
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
