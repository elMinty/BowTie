package project.bowtie.App.Controllers.PathPane;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import project.bowtie.App.Controllers.ViewPane.NodeController;
import project.bowtie.Model.BTmodel.Nodes.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PathController class
 * This class is the controller for the PathPane
 * It handles the attacks and consequences
 * Also allows user to view nodes
 */
public class PathPaneController {

    // Panes
    @FXML public TabPane mainTabPane;
    @FXML public AnchorPane leftPane;
    @FXML public AnchorPane rightPane;
    @FXML public TabPane TabPane;

    // List of attacks
    @FXML public ListView<String> listViewNodes = new ListView<>();
    public SplitPane pathPane;
    public ListView availableNodes;
    @FXML private ListView<String> listViewAttacks = new ListView<>();
    @FXML private ListView<String> listViewConsequences = new ListView<>();

    // Text area for details
    @FXML public TextArea textArea;

    // Node Controller
    private NodeController nc;
    private Node TopEvent;
    private Map<String, Node> map;

    public Map<String, String> attackMap = new HashMap<String, String>();
    public Map<String, String> consequenceMap = new HashMap<String, String>();

    public Map<String, List<Integer>> attackMapIds = new HashMap<String, List<Integer>>();
    public Map<String, List<Integer>> consequenceMapIds = new HashMap<String, List<Integer>>();

    /**
     * Initializes the PathPane
     */
    public void initialize() {
        textArea.setEditable(false);
        System.out.println(listViewNodes);
        initListeners();
    }

    /**
     * Initializes the listeners for the PathPane
     */
    private void initListeners() {
        listViewNodes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String id = newValue.split(": ")[1];
                updateNodeTextArea(map.get(id));
            }
        });

        availableNodes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String id = newValue.toString().split(": ")[1];
                updateNodeTextArea(map.get(id));
            }
        });

        listViewAttacks.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String id = newValue.split(": ")[0];
                updateAttackPathTextArea(attackMap.get(id));
                availableNodes.getItems().clear();
                for (int i : attackMapIds.get(id)) {
                    String idString = String.valueOf(i);
                    String name = map.get(idString).getName();
                    availableNodes.getItems().add(name + " : " + idString);
                }
            }
        });

        listViewConsequences.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String id = newValue.split(": ")[0];
                updateConsequencePathTextArea(consequenceMap.get(id));
                availableNodes.getItems().clear();
                for (int i : consequenceMapIds.get(id)) {
                    String idString = String.valueOf(i);
                    String name = map.get(idString).getName();
                    availableNodes.getItems().add(name + " : " + idString);
                }
            }
        });
    }

    /**
     * Updates the text area with the details of the selected node
     * @param node the selected node
     */
    private void updateNodeTextArea(Node node) {
        textArea.clear();
        // Turn Before Nodes into String List seperated with commas and without trailing comma
        // Construct the text to display based on the selected node
        String displayText = "Node ID: " + node.getId() + "\n\n\n" +
                "Node Name: " + node.getName() + "\n\n\n" +
                "Node Type: " + node.getType() + "\n\n\n" +
                "Node Description: " + node.getDescription() + "\n\n\n" +
                // Add any other details you want to display
                "Node Score: " + node.getScore() + "\n\n\n" +
                "Requisite (Before) Nodes: " + nodeMapToString(node.getBeforeNodes()) + "\n\n\n" +
                "Consequence (After) Nodes: " + nodeMapToString(node.getAfterNodes()) + "\n\n\n" +
                "Mitigation Nodes: " + nodeMapToString(node.getMitigationNodes()) + "\n\n\n";

        textArea.setText(displayText);
    }

    private void updateAttackPathTextArea(String attackPath) {
        textArea.clear();
        attackPath = replaceIDs(attackPath);
        // Construct the text to display based on the selected node
        String displayText = "Attack Path:\n\n\t\t" + attackPath.replace("\n", "\n\t\t") + "\n\n\n";
        textArea.setText(displayText);
    }

    private void updateConsequencePathTextArea(String consequencePath) {
        textArea.clear();
        consequencePath = replaceIDs(consequencePath);
        // Construct the text to display based on the selected node
        String displayText = "Consequence Path:\n\n\t\t" + consequencePath.replace("\n", "\n\t\t") + "\n\n\n";
        textArea.setText(displayText);
    }

    /**
     * nodeMap setter
     * @param nodeMap the node map
     */
    public void setNodeMap(Map<String, Node> nodeMap) {
        this.map = nodeMap;
    }

    /**
     * Updates the node list
     * @param id the id of the node
     * @param isAdd whether to add or remove the node
     */
    public void updateNodeList(String id, boolean isAdd) {
        Node node = map.get(id);
        if (node != null) {
            String listName = node.getType() + " - " + node.getName() + " : " + node.getId();
            if (isAdd) {
                listViewNodes.getItems().add(listName);
            } else {
                listViewNodes.getItems().remove(listName);
            }
        }
    }

    /**
     * Update path maps for attacks and consequences
     * @param attackPathMap the attack path map
     */
    public void updatePathMaps(List<String> attackPathMap, List<String> consequencePathMap, List<List<Integer>> attackPath, List<List<Integer>> consequencePath) {
        // Clear the text area and list views
        textArea.clear();
        listViewAttacks.getItems().clear();
        listViewConsequences.getItems().clear();

        //clear maps
        attackMap.clear();
        consequenceMap.clear();

        listViewAttacks.getItems().clear();
        listViewConsequences.getItems().clear();
        int counter = 0;
        for (String attack : attackPathMap) {
            listViewAttacks.getItems().add(String.valueOf(counter));
            attackMap.put(Integer.toString(counter), attack);
            attackMapIds.put(Integer.toString(counter), attackPath.get(counter));
            counter++;
        }
        int counter2 = 0;
        for (String consequence : consequencePathMap) {
            listViewConsequences.getItems().add(String.valueOf(counter2));
            consequenceMap.put(Integer.toString(counter2), consequence);
            consequenceMapIds.put(Integer.toString(counter2), consequencePath.get(counter2));
            counter2++;
        }
    }

    /**
     * Converts a map to a string
     * @param map the map
     * @return the string representation of the map
     */
    public static String nodeMapToString(Map<String, Node> map) {
        if (map.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Node> entry : map.entrySet()) {
            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }
            Node node = entry.getValue();
            builder.append(node.getName());
        }
        return builder.toString();
    }

    public String replaceIDs(String path) {
        // Pattern to match integer IDs
        Pattern pattern = Pattern.compile("\\b\\d+\\b");
        Matcher matcher = pattern.matcher(path);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            Integer id = Integer.valueOf(matcher.group());
            if (map.containsKey(String.valueOf(id))) {
                String replacement = map.get(String.valueOf(id)).getName(); // Use the ID from map, or original if not found
                matcher.appendReplacement(sb, replacement);
            }
            else {
                matcher.appendReplacement(sb, matcher.group());
            }
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}
