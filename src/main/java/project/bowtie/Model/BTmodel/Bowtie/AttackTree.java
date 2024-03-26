package project.bowtie.Model.BTmodel.Bowtie;

import javafx.util.Pair;
import project.bowtie.Model.BTmodel.Nodes.Node;
import project.bowtie.Model.BTmodel.Nodes.path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
     * @param topEvent The top event of the tree - type node
     */
    public void setTopEvent(Node topEvent) {
        this.TopEvent = topEvent;
    }

    /**
     * Getter for the top event of the tree - if the tree is null, create a new instance
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
     *
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


    // Adjusted to work with the updated path class
    public static void findAllPaths(Node node, path currentPath, List<path> allPaths) {
        // Create a new pair for the current node
        Pair<String, String> nodePair = new Pair<>(node.getId(), node.getType().toString());

        // Check if the current node is an "AND" node and update the path's containsAnd flag
        if (node.getType().toString().equals("AND")) {
            currentPath.containsAnd = true;
        }

        // Add the current node to the path
        currentPath.addBeforeNode(nodePair);

        // Check if it's a leaf node
        if (node.getBeforeNodes().isEmpty()) {
            // Clone the currentPath to preserve its state and add it to allPaths
            allPaths.add(new path(currentPath));
        } else {
            // If not, recurse on each beforeNode
            for (Node beforeNode : node.getBeforeNodes().values()) {
                findAllPaths(beforeNode, new path(currentPath), allPaths);
            }
        }
    }

    // Utility method to start the search from the top event
    public static List<path> findPathsFromTopEvent(Node topEvent) {
        List<path> allPaths = new ArrayList<>();
        findAllPaths(topEvent, new path(), allPaths);

        // set id for each path
        for (int i = 0; i < allPaths.size(); i++) {
            allPaths.get(i).setId(i);
        }
        return allPaths;
    }

    public static boolean printPaths(List<path> paths) {
        for (int i = 0; i < paths.size(); i++) {
            System.out.println(path.toPathString(paths.get(i).getNodes(), true));
        }
        return true;
    }

    // return all paths that contains and
    public static List<path> findPathsContainingAnd(List<path> paths) {
        List<path> pathsContainingAnd = new ArrayList<>();
        for (path p : paths) {
            if (p.containsAnd) {
                pathsContainingAnd.add(p);
            }
        }
        return pathsContainingAnd;
    }

    // return all the and node ids
    public static List<String> findAndNodeIds(List<path> pathsContainingAnd) {
        // Create a set to store the unique AND node ids
        List<String> andNodeIds = new ArrayList<>();
        for (path p : pathsContainingAnd) {
            for (Pair<String, String> node : p.getNodes()) {
                if (node.getValue().equals("AND") && !andNodeIds.contains(node.getKey())) {
                    andNodeIds.add(node.getKey());
                }
            }
        }

        return andNodeIds;
    }

    public static List<List<path>> segmentPaths(List<path> paths) {
        List<List<path>> segmentedPaths = new ArrayList<>();

        for (path originalPath : paths) {
            List<path> segments = new ArrayList<>();
            path currentSegment = new path();
            boolean segmentStarted = false;

            for (Pair<String, String> node : originalPath.getNodes()) {
                if (node.getValue().equals("AND")) {
                    if (segmentStarted) {
                        currentSegment.addBeforeNode(node); // Add AND node to the current segment
                        segments.add(new path(currentSegment)); // End and add the current segment
                        currentSegment = new path(); // Start a new segment
                    }
                    currentSegment.addBeforeNode(node); // Add AND node at the start of new segment
                    segmentStarted = true;
                } else {
                    currentSegment.addBeforeNode(node);
                    segmentStarted = true;
                }
            }
            if (!currentSegment.getNodes().isEmpty()) {
                segments.add(currentSegment); // Add the last segment if not empty
            }
            segmentedPaths.add(segments);
        }

        return segmentedPaths;
    }


    public void getPaths() {

        List<path> paths = findPathsFromTopEvent(this.TopEvent);
        printPaths(paths);

        // print TopEvent id
        System.out.println("TopEvent id: " + this.TopEvent.getId());
        //print TopEvent before nodes
        System.out.println("TopEvent before nodes: " + this.TopEvent.getBeforeNodes().keySet());

        List<path> pathsContainingAnd1 = findPathsContainingAnd(paths);
        //print paths containing and size
        System.out.println("Paths containing AND size: " + pathsContainingAnd1.size());

        List<path> pathsContainingAnd = path.reversePaths(pathsContainingAnd1);

        List<List<path>> segmentedPaths = segmentPaths(pathsContainingAnd);
        //print segmented paths size
        System.out.println("Segmented paths size: " + segmentedPaths.size());

        //print all path segments
        for (int i = 0; i < segmentedPaths.size(); i++) {
            // print all segments for each path
            for (int j = 0; j < segmentedPaths.get(i).size(); j++) {
                System.out.println("Path " + i + " Segment " + j + ": " + path.toPathString(segmentedPaths.get(i).get(j).getNodes(), true));
            }
        }
    }

    public List<path> generateLogicalPaths() {





        return findPathsContainingAnd(findPathsFromTopEvent(this.TopEvent));
    }



}
