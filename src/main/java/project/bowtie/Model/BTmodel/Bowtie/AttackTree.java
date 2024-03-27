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

    public List<Object> retrieveAllAttacks() {
        return retrieveAttacks(this.TopEvent, null);
    }

    // Recursive method to traverse the tree and retrieve attacks
    private static List<Object> retrieveAttacks(Node node, String parentAndId) {
        List<Object> paths = new ArrayList<>();

        // Base case: if the node is a leaf node
        if (node.isRootNode()) {
            paths.add(node.getId());
            return paths;
        }

        Map<String, Node> beforeNodes = node.getBeforeNodes();

        if (node.getType() == NodeType.AND) {
            String andId = "AND_" + UUID.randomUUID().toString(); // Unique identifier for this AND operation
            List<List<Object>> combinedPaths = new ArrayList<>();
            combinedPaths.add(new ArrayList<>()); // Start with a single empty path

            for (Node child : beforeNodes.values()) {
                List<Object> childPaths = retrieveAttacks(child, andId);
                List<List<Object>> newCombinedPaths = new ArrayList<>();

                for (List<Object> currentPath : combinedPaths) {
                    for (Object childPath : childPaths) {
                        List<Object> newPath = new ArrayList<>(currentPath);
                        if (!(childPath instanceof String) || !newPath.isEmpty()) {
                            // This check prevents adding unnecessary AND_JOIN markers
                        }
                        newPath.add(childPath);
                        newCombinedPaths.add(newPath);
                    }
                }
                combinedPaths = newCombinedPaths;
            }

            // If this AND operation is nested within another AND, wrap combinedPaths
            if (parentAndId != null) {
                List<Object> wrappedCombinedPaths = new ArrayList<>();
                wrappedCombinedPaths.add(andId); // Start with AND identifier
                wrappedCombinedPaths.addAll(combinedPaths);
                paths.addAll(wrappedCombinedPaths);
            } else {
                paths.addAll(combinedPaths);
            }
        } else {
            for (Node child : beforeNodes.values()) {
                List<Object> childPaths = retrieveAttacks(child, parentAndId);
                paths.addAll(childPaths);
            }
        }

        // Prepend the current node only if it's not part of an AND operation
        if (parentAndId == null) {
            List<Object> newPath = new ArrayList<>();
            newPath.add(node.getId());
            newPath.add(paths);
            return List.of(newPath);
        } else {
            return paths;
        }
    }

    public List<String> generateAllPaths(){
        System.out.println(generatePaths(this.TopEvent));
        return generatePaths(this.TopEvent);
    }

    private List<String> generatePaths(Node node){
        //return empty List if node is null
        if (node == null) {
            System.out.println("Node is null");
            return new ArrayList<>();
        }

        // new paths list
        List<String> paths = new ArrayList<>();

        // if start point add to paths
        if (node.isRootNode()){
            paths.add(node.getId());
        }

        // if AND node
        if (node.getType() == NodeType.AND){
            // new child paths and get all children paths of AND
            // List of each path associated with the child
            Map<Node, List<String>> pathsFromChild = new HashMap<>();
            for (Node child : node.getBeforeNodes().values()){
                pathsFromChild.put(child, generatePaths(child));
            }
            paths.addAll(generatePathCombinations(pathsFromChild));


        } else {
            // for children of OR
            for (Node child : node.getBeforeNodes().values()){
                // for paths in the generated paths
                List<String> newPaths = generatePaths(child);
                for (String path : newPaths){
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
