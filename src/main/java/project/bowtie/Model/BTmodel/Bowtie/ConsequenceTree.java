package project.bowtie.Model.BTmodel.Bowtie;

import project.bowtie.Model.BTmodel.Nodes.*;

public class ConsequenceTree extends Tree {
    private static ConsequenceTree instance;


    public ConsequenceTree(Node rootNode) {
        super(rootNode);

    }

    public static ConsequenceTree getInstance(Node rootNode) {
        if (instance == null) {
            instance = new ConsequenceTree(rootNode);
        }
        return instance;
    }

    public static int findMinimumDepth(Node root) {
        if (root == null) return 0;
        if (root.isConsequenceLeaf()) return 1;

        int minDepth = Integer.MAX_VALUE;
        for (Node child : root.getAfterNodes().values()) {
            minDepth = Math.min(minDepth, findMinimumDepth(child));
        }
        return minDepth + 1;
    }

    public static int findMaximumDepth(Node root) {
        if (root == null) return 0;
        if (root.isConsequenceLeaf()) return 1;

        int maxDepth = 0;
        for (Node child : root.getAfterNodes().values()) {
            maxDepth = Math.max(maxDepth, findMaximumDepth(child));
        }
        return maxDepth + 1;
    }

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
