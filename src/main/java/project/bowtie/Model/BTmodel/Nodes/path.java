package project.bowtie.Model.BTmodel.Nodes;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class path {
    private List<Pair<String, String>> nodes;
    public int id;
    public boolean containsAnd = false;

    public path() {
        this.nodes = new ArrayList<>();
    }

    public path(List<Pair<String, String>> nodes, int id) {
        this.nodes = nodes;
    }

    public path(path path) {
        this.nodes = new ArrayList<>(path.getNodes());
        this.id = path.id;
        this.containsAnd = path.containsAnd;

    }

    public List<Pair<String, String>> getNodes() {
        return nodes;
    }

    public void setNodes(List<Pair<String, String>> nodes) {
        this.nodes = nodes;
    }

    public void addAfterNode(Pair<String, String> node) {
        // adds node to end of list
    }

    public void addBeforeNode(Pair<String, String> node) {
        // adds node to beggining of list pushing all other nodes to the right
        this.nodes.add(0, node);
    }

    // Node is the Pair<String, String> node
    public static String getNode(String node) {
        return node.split(" ")[0];
    }

    public static String getType(String node) {
        return node.split(" ")[1];
    }

    public static String toPathString(List<Pair<String, String>> nodes, boolean isAttackTree) {
        String path = "";
        String arrow = " -> ";
        if (isAttackTree) {
            // reverse the list
            List<Pair<String, String>> reversed = new ArrayList<>();
            for (int i = nodes.size() - 1; i >= 0; i--) {
                reversed.add(nodes.get(i));
            }
            nodes = reversed;
        }
        for (Pair<String, String> node : nodes) {
            path += "(" + node.getKey() + " " + node.getValue() + ")" + arrow;
        }
        return path;
    }

    public void setId(int i) {
        this.id = i;
    }

    public static List<path> reversePaths(List<path> paths) {
        List<path> reversedPaths = new ArrayList<>();
        for (path p : paths) {
            List<Pair<String, String>> reversedNodes = new ArrayList<>();
            for (int i = p.getNodes().size() - 1; i >= 0; i--) {
                reversedNodes.add(p.getNodes().get(i));
            }
            reversedPaths.add(new path(reversedNodes, p.id));
        }

        // reverse order of paths
        List<path> reversedOrder = new ArrayList<>();
        for (int i = reversedPaths.size() - 1; i >= 0; i--) {
            reversedOrder.add(reversedPaths.get(i));
        }

        return reversedOrder;
    }
}


