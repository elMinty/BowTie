package project.bowtie.Model.BTmodel.Bowtie;

import project.bowtie.Model.BTmodel.Nodes.*;

import java.util.*;

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
    // New Paths functions \\
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Generate all paths in the consequence tree
     * @return List of all paths in the tree
     */
    public List<String> generateAllPaths(){
        System.out.println(generatePaths(this.TopEvent));
        return generatePaths(this.TopEvent);
    }

    /**
     * Generate all paths from a node in the tree
     * @param node The node to generate paths from
     * @return List of all paths in the tree from the node
     */
    private List<String> generatePaths(Node node){
        //return empty List if node is null
        if (node == null) {
            System.out.println("Node is null");
            return new ArrayList<>();
        }

        // new paths list
        List<String> paths = new ArrayList<>();

        // if start point add to paths
        if (node.isLeafNode()){
            paths.add(node.getId());
        }

        // if AND node
        if (node.getType() == NodeType.AND){
            // new child paths and get all children paths of AND
            // List of each path associated with the child

            // Check Backwards children

            List<String> backwardsChildren = new ArrayList<>();
            for (Node child : node.getBeforeNodes().values()){
                backwardsChildren.add(child.getId());
            }

            // make a string of the backwards children
            String backwardsPath = "REQUIRES(" + String.join(", ", backwardsChildren) + ")";

            // Check Forwards children

            Map<Node, List<String>> forwardsChildren = new HashMap<>();
            for (Node child : node.getAfterNodes().values()){
                forwardsChildren.put(child, generatePaths(child));
            }
            List<String> new_paths = generatePathCombinations(forwardsChildren);

            for (String path : new_paths){
                path = backwardsPath + "->" + path;
                paths.add(path);
            }


        } else {
            // for children of OR
            for (Node child : node.getAfterNodes().values()){
                // for paths in the generated paths

                List<String> newPaths = generatePaths(child);
                if (child.getType() == NodeType.AND){
                    paths.addAll(newPaths);
                    continue;
                }
                for (String path : newPaths){
                    paths.add(node.getId() + "->" + path);
                }
            }
        }
        return paths;
    }

    /**
     * Generate all path combinations for an AND node
     * @param pathsByNode Map of paths by node
     * @return List of all path combinations
     */
    public static List<String> generatePathCombinations(Map<Node, List<String>> pathsByNode) {
        List<List<String>> allPaths = new ArrayList<>(pathsByNode.values());
        List<String> combinations = new ArrayList<>();
        combinePathsRecursive(allPaths, 0, "", combinations);
        return combinations;
    }

    /**
     * Combine paths recursively to get an ALL list for paths from an AND node
     * @param allPaths List of all paths
     * @param depth Depth of the tree
     * @param current Current path
     * @param combinations List of all path combinations
     */
    private static void combinePathsRecursive(List<List<String>> allPaths, int depth, String current, List<String> combinations) {
        if (depth == allPaths.size()) {
            combinations.add("ALL(" + current + ")");
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
