package project.bowtie.Model.BTmodel.Bowtie;

import javafx.util.Pair;
import project.bowtie.Model.BTmodel.Nodes.Node;
import project.bowtie.Model.BTmodel.Nodes.NodeType;
import project.bowtie.Model.BTmodel.Nodes.path;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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
    private List<path> paths = new ArrayList<>();
    private static AttackTree instance;
    private Node TopEvent;
    private List<String> attacks;

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

    private static List<Integer> extractIDs(String path) {
        List<Integer> integers = new ArrayList<>();
        Pattern pattern = Pattern.compile("-?\\d+"); // Regex to match integers
        Matcher matcher = pattern.matcher(path);
        while (matcher.find()) {
            integers.add(Integer.parseInt(matcher.group()));
        }
        return integers;
    }

    public List<List<Integer>> generatePathIDs() {
        List<List<Integer>> paths = new ArrayList<>();
        for (String attack : attacks) {
            paths.add(extractIDs(attack));
        }
        return paths;
    }

    public List<String> generateAllPaths() {
        System.out.println(generatePaths(this.TopEvent));
        attacks = generatePaths(this.TopEvent);
        List<String> formattedPaths = new ArrayList<>();
        for (String attack : attacks) {
            formattedPaths.add(attackFormatter.formatAttack(reversePath(attack)));
        }

        return formattedPaths;
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
                    paths.add(path + "->" + node.getId());
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

    private static String formatAndElements(String path) {
        return formatNestedAnd(path, 0);
    }

    private static String formatNestedAnd(String path, int level) {
        StringBuilder formattedPath = new StringBuilder();
        int andStart = path.indexOf("AND(");

        if (andStart == -1) {
            // No more ANDs, process the remaining flow
            return formatFlow(path, level);
        }

        while (andStart != -1) {
            int andEnd = findClosingParenthesisIndex(path, andStart + 4);
            String andContent = path.substring(andStart + 4, andEnd);
            String afterAnd = path.substring(andEnd + 1).trim();

            // Process AND content recursively
            if (level > 0) formattedPath.append("\t".repeat(level - 1)).append("+ ");
            formattedPath.append("AND:\n");
            String[] elements = andContent.split(", ");
            for (String element : elements) {
                if (element.contains("AND")) {
                    // Recursively process nested AND
                    formattedPath.append(formatNestedAnd(element, level + 1));
                } else if (element.contains("->")) {
                    // Process flow within AND
                    formattedPath.append(formatFlow(element, level + 1));
                } else {
                    // Process single elements within AND
                    formattedPath.append("\t".repeat(level)).append("+ ").append(element).append("\n");
                }
            }

            path = afterAnd;
            andStart = path.indexOf("AND(");
        }

        // Process remaining path after last AND
        if (!path.isEmpty()) {
            formattedPath.append(formatFlow(path, level));
        }

        return formattedPath.toString();
    }

    private static String formatFlow(String flow, int level) {
        StringBuilder formattedFlow = new StringBuilder();
        String[] elements = flow.split("->");
        for (int i = 0; i < elements.length; i++) {
            if (i == 0) {
                formattedFlow.append("\t".repeat(level)).append("-> ").append(elements[i].trim());
            } else {
                formattedFlow.append(" -> ").append(elements[i].trim());
            }
        }
        formattedFlow.append("\n");
        return formattedFlow.toString();
    }

    private static int findClosingParenthesisIndex(String str, int start) {
        int counter = 1;
        for (int i = start; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                counter++;
            } else if (str.charAt(i) == ')') {
                counter--;
                if (counter == 0) {
                    return i;
                }
            }
        }
        return -1; // Not found (unbalanced parentheses)
    }
}

class attackFormatter {
    private static final Map<String, String> andDescriptions = new LinkedHashMap<>();
    private static int counter;

    public static String formatAttack(String path) {
        counter = 1;
        andDescriptions.clear();
        String formattedPath = parseAnds(path);
        StringBuilder output = new StringBuilder();

        andDescriptions.forEach((key, value) -> output.append(key).append(": ").append(value).append("\n"));
        output.append("\nPath: ").append(formattedPath);

        return output.toString();
    }

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

