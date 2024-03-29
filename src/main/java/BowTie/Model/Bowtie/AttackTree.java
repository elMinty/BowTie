package BowTie.Model.Bowtie;

import BowTie.Model.Nodes.Node;
import BowTie.Model.Nodes.NodeType;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Attack Tree class - provides tree structure for before the Top-Event
 * Used in the Bowtie Class
 *
 * @see Bowtie
 * @see Tree
 */
public class AttackTree extends Tree {
    private HashMap<String, Node> rootNodes = new HashMap<>();
    private static AttackTree instance;
    private Node TopEvent;
    private List<String> attacks;
    private static final Map<String, String> andDescriptions = new LinkedHashMap<>();
    private static int counter;

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

    /**
     * Extract IDs from a path
     *
     * @param path The path to extract IDs from
     * @return List of IDs in the path
     */
    private static List<Integer> extractIDs(String path) {
        List<Integer> integers = new ArrayList<>();
        Pattern pattern = Pattern.compile("-?\\d+"); // Regex to match integers
        Matcher matcher = pattern.matcher(path);
        while (matcher.find()) {
            integers.add(Integer.parseInt(matcher.group()));
        }
        return integers;
    }

    /**
     * Generate path IDs
     *
     * @return List of all path IDs in the tree
     */
    public List<List<Integer>> generatePathIDs() {
        List<List<Integer>> paths = new ArrayList<>();
        for (String attack : attacks) {
            paths.add(extractIDs(attack));
        }
        return paths;
    }

    /**
     * Generate all paths in the attack tree
     *
     * @return List of all paths in the tree
     */
    public List<String> generateAllPaths() {
        System.out.println(generatePaths(this.TopEvent));
        attacks = generatePaths(this.TopEvent);
        List<String> formattedPaths = new ArrayList<>();
        for (String attack : attacks) {
            formattedPaths.add(formatAttack(reversePath(attack)));
        }

        return formattedPaths;
    }

    /**
     * Generate all paths from a node in the tree
     *
     * @param node The node to generate paths from
     * @return List of all paths in the tree from the node
     */
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
                    paths.add(path + "->" + node.getId());
                }
            }
        }
        return paths;
    }

    /**
     * Generate path combinations
     *
     * @param pathsByNode The paths by node
     * @return The list of path combinations
     */
    public static List<String> generatePathCombinations(Map<Node, List<String>> pathsByNode) {
        List<List<String>> allPaths = new ArrayList<>(pathsByNode.values());
        List<String> combinations = new ArrayList<>();
        combinePathsRecursive(allPaths, 0, "", combinations);
        return combinations;
    }

    /**
     * Combine paths recursively
     *
     * @param allPaths      The paths to combine
     * @param depth         The current depth
     * @param current       The current path
     * @param combinations  The list of combinations
     */
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

    /**
     * Reverse the path and change the arrow directions
     *
     * @param path The path to reverse
     * @return The reversed path
     */
    public static String reversePath(String path) {
        // Split the path into segments based on "<-"
        String[] segments = path.split("<-");
        StringBuilder reversedPath = new StringBuilder();
        // Reverse the order of segments and change arrow directions
        for (int i = segments.length - 1; i >= 0; i--) {
            reversedPath.append(segments[i]);
            if (i > 0) { // Add "->" to join segments except for the last segment
                reversedPath.append(" -> ");
            }
        }
        return reversedPath.toString();
    }


    /**
     * Format the attack path
     *
     * @param path The path to format
     * @return The formatted path
     */
    public static String formatAttack(String path) {
        counter = 1;
        andDescriptions.clear();
        String formattedPath = parseAnds(path);
        StringBuilder output = new StringBuilder();

        andDescriptions.forEach((key, value) -> output.append(key).append(": ").append(value).append("\n"));
        output.append("\nPath: ").append(formattedPath);

        return output.toString();
    }

    /**
     * Parse the AND nodes in the path
     *
     * @param segment The segment to parse
     * @return The parsed segment
     */
    private static String parseAnds(String segment) {
        int startIdx = segment.indexOf("AND(");
        while (startIdx != -1) {
            int endIdx = findClosingParenthesisIndex(segment, startIdx + 4);
            String innerSegment = segment.substring(startIdx + 4, endIdx);

            // Parse the inner segment for further ANDs
            String parsedInnerSegment = parseAnds(innerSegment);

            String andLabel = "AND" + counter++;
            andDescriptions.put(andLabel, parsedInnerSegment);

            segment = segment.substring(0, startIdx) + andLabel + segment.substring(endIdx + 1);
            startIdx = segment.indexOf("AND(", startIdx + andLabel.length());
        }

        return segment.replace("->", " -> ");
    }

    /**
     * Find the index of the closing parenthesis
     *
     * @param str   The string to search
     * @param start The starting index
     * @return The index of the closing parenthesis
     */
    private static int findClosingParenthesisIndex(String str, int start) {
        int depth = 1;
        for (int i = start; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                depth++;
            } else if (str.charAt(i) == ')') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1; // Not found (unbalanced parentheses)
    }

}
