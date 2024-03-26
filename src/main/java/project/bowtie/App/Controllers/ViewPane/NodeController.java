package project.bowtie.App.Controllers.ViewPane;

import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import project.bowtie.App.Controllers.ViewPane.Menus.NodeContextMenu;
import project.bowtie.App.Controllers.ViewPane.Menus.ViewOption;
import project.bowtie.App.Controllers.ViewPane.Obj.Lines.Connector;
import project.bowtie.App.Controllers.ViewPane.Obj.Nodes.NodeFactory;
import project.bowtie.App.Controllers.ViewPane.Obj.Nodes.NodeLabel;
import project.bowtie.Model.BTmodel.Nodes.Node;
import project.bowtie.Model.BTmodel.Nodes.NodeType;
import project.bowtie.Model.BTmodel.Nodes.NodeUtils;
import project.bowtie.Model.BTmodel.Nodes.NodeDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for the nodes and shapes in the view pane
 * Manages the nodes in the view pane and connections for the bowtie model
 *
 * @see project.bowtie.App.Controllers.ViewPaneController
 * @see project.bowtie.Model.BTmodel.Nodes.Node
 *
 * Uses Connector to manage connections
 *
 * @see project.bowtie.App.Controllers.ViewPane.Obj.Lines.Connector
 *
 * Uses NodeContextMenu to manage context menu options
 *
 * @see project.bowtie.App.Controllers.ViewPane.Menus.NodeContextMenu
 *
 * Uses NodeLabel to manage labels for nodes
 *
 * @see project.bowtie.App.Controllers.ViewPane.Obj.Nodes.NodeLabel
 *
 * @version 1.0
 *
 */
public class NodeController {

    Pane root;
    NodeContextMenu nodeContextMenu;
    private Map<String, Node> nodeMap = new HashMap<String, Node>(); // Map of nodes
    private int nodeCount = 0; // Node count
    public Connector connector; // Connector for connecting nodes
    private String userInput = ""; // User input for editing node details


    /**
     * Constructor for the NodeController
     *
     * @param root the root pane
     */
    public NodeController(AnchorPane root) {
        this.root = root;
        nodeContextMenu = new NodeContextMenu(this);
        connector = new Connector(nodeMap);
    }

    /**
     * Handles the delete operation for a shape - removes from pane and the bowtie model
     *
     * @param shape the shape to delete
     */
    public void handleDelete(Shape shape) {
        // Ensure shape exists
        if (shape.getUserData() != null) {
            Node node = (Node) shape.getUserData();

            // WARNING!! Top Event cannot be deleted
            if (node.getType() == NodeType.TOP_EVENT) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("TopEvent");
                alert.setContentText("Top Event cannot be deleted.");
                alert.showAndWait();
            }

            // Otherwise, remove it from the node map
            else {
                // Check for all connections BEFORE AND AFTER and disconnect - deleting all connecting lines
                for (Node beforeNode : node.getBeforeNodes().values()) {
                    if (beforeNode != null) {
                        connector.disconnect(beforeNode.getId(), node.getId());
                    }
                }
                for (Node afterNode : node.getAfterNodes().values()) {
                    if (afterNode != null) {
                        connector.disconnect(node.getId(), afterNode.getId());
                    }
                }

                NodeLabel nameLabel = node.getNameLabel();
                NodeLabel descriptionLabel = node.getDescriptionLabel();
                nodeMap.remove(node.getId());

                NodeUtils.delete(node);
                root.getChildren().removeAll(shape, nameLabel, descriptionLabel);
            }
        }
    }

    /**
     * Handles the scale operation for a shape - scales the shape
     *
     * @param shape the shape to scale
     */
    public void handleScale(Shape shape) {
        TextInputDialog dialog = new TextInputDialog("1.0");
        dialog.setTitle("Scale Shape");
        dialog.setHeaderText("Enter Scale Factor");
        dialog.setContentText("Scale:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(scaleFactor -> {
            try {
                double scale = Double.parseDouble(scaleFactor);
                shape.setScaleX(scale);
                shape.setScaleY(scale);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Handles the add node operation - adds a node to the view pane
     *
     * @param type the type of node to add
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the shape of the node
     *
     * @see project.bowtie.Model.BTmodel.Nodes.NodeType
     * @see project.bowtie.App.Controllers.ViewPane.Obj.Nodes.NodeFactory
     */
    public Shape handleAddNode(NodeType type, double x, double y) {

        //init Node
        Shape shape = NodeFactory.createNode(type);
        setNodeListeners(shape);
        Node node = new Node(String.valueOf(nodeCount), type, type.toString() + nodeCount);

        // Set the shape's position
        shape.setLayoutX(x);
        shape.setLayoutY(y);

        // link pane and node
        shape.setId(node.getId());
        nodeMap.put(node.getId(), node);

        //init labels
        NodeLabel nameLabel = new NodeLabel(type.toString() + nodeCount, shape, NodeDetail.NAME, type);
        NodeLabel infoLabel = new NodeLabel("", shape, NodeDetail.DESCRIPTION, type);
        NodeLabel scoreLabel = new NodeLabel("", shape, NodeDetail.SCORE, type);
        node.setNameLabel(nameLabel);
        node.setDescriptionLabel(infoLabel);
        node.setScoreLabel(scoreLabel);

        // link shape on viewPane and node
        shape.setUserData(node);
        // update node count
        nodeCount++;

        // Add the shape to the root
        root.getChildren().addAll(shape, infoLabel, nameLabel, scoreLabel);

        return shape;
    }

    /**
     * Sets the listeners for a shape
     * @param shape
     */
    private void setNodeListeners(Shape shape) {
        // Drag and drop listeners

        // Initial drag offset
        final double[] offsetX = {0};
        final double[] offsetY = {0};

        // Set mouse pressed event
        shape.setOnMousePressed(event -> {
            // Calculate the offset from the top-left corner of the rectangle
            // Adjust by the current scale of the root pane to ensure consistency at different zoom levels
            offsetX[0] = (event.getSceneX() - shape.getLayoutX()) / root.getScaleX();
            offsetY[0] = (event.getSceneY() - shape.getLayoutY()) / root.getScaleY();
        });

        // Set mouse dragged event
        shape.setOnMouseDragged(event -> {
            // Update the position of the rectangle
            // Consider the current scale of the root pane to match the drag distance with mouse movement
            shape.setLayoutX((event.getSceneX() - offsetX[0] * root.getScaleX()));
            shape.setLayoutY((event.getSceneY() - offsetY[0] * root.getScaleY()));
        });

        // Set context menu event
        shape.setOnContextMenuRequested(event -> {
            nodeContextMenu.showContextMenu(shape, event.getScreenX(), event.getScreenY());
            event.consume();  // Add this line to consume the event
        });

        // Connect Check
        shape.setOnMouseClicked(event -> {
            // Check if connect mode is enabled
            if (connector.getConnectMode()) {
                String targetNodeId = shape.getId(); // currentNode represents the node that opened the context menu
                connector.setTargetNodeId(targetNodeId);
                connector.flush(shape);
            }
        });
    }

    /**
     * Handles the edit operation for a shape - opens a popup window for editing the node's name, description, or quantifier
     *
     * @param shape the shape to edit
     * @param detail the detail to edit
     *
     * @see project.bowtie.Model.BTmodel.Nodes.NodeDetail
     */
    public void handleEdit(Shape shape, NodeDetail detail) {

        // gets a window popup for entering a string to edit the node's name, description, or quantifier
        double offsetX = shape.getLayoutX();
        double offsetY = shape.getLayoutY();

        // Create a TextField for user input
        TextField textField = new TextField();
        textField.setPromptText("Enter your text here");
        textField.setLayoutX(100 + offsetX); // Set X position
        textField.setLayoutY(100 + offsetY); // Set Y position

        // Create a Button to submit the input
        Button submitButton = new Button("Submit");
        submitButton.setLayoutX(250 + offsetX); // Set X position
        submitButton.setLayoutY(100 + offsetY); // Set Y position

        Node node = (Node) shape.getUserData();

        switch (detail) {
            case NAME:
                handleName(node, textField, submitButton);

                break;
            case DESCRIPTION:
                handleDescription(node, textField, submitButton);

                break;
            case SCORE:
                // score handling
                handleScoring(shape);

                break;
            default:
                break;
        }
    }

    /**
     * Handles the view operation for a shape - toggles the visibility of the node's name, description, or quantifier
     *
     * @param shape the shape to view
     * @param detail the detail to view
     * @param viewOption the view option
     *
     * @see project.bowtie.Model.BTmodel.Nodes.NodeDetail
     * @see project.bowtie.App.Controllers.ViewPane.Menus.ViewOption
     */
    public void handleView(Shape shape, NodeDetail detail, ViewOption viewOption) {
        switch (detail) {
            case NAME:
                NodeLabel nameLabel = ((Node) shape.getUserData()).getNameLabel();
                nameLabel.toggleLabelVisibility(viewOption);
                break;
            case DESCRIPTION:
                NodeLabel descriptionLabel = ((Node) shape.getUserData()).getDescriptionLabel();
                descriptionLabel.toggleLabelVisibility(viewOption);

                break;
            case SCORE:
                NodeLabel scoreLabel = ((Node) shape.getUserData()).getScoreLabel();
                scoreLabel.toggleLabelVisibility(viewOption);

                break;
            default:
                break;
        }
    }

    /**
     * gets a node from the node map
     *
     * @param id the id of node to get
     */
    public Node getNode(String id) {
        return nodeMap.get(id);
    }

    /**
     * resets the user input
     */
    private void resetUserInput() {
        userInput = "";
    }

    /**
     * handles the name of a node for editing
     *
     * @param node the node to handle
     * @param textField the text field
     * @param submitButton the submit button
     */
    private void handleName(Node node, TextField textField, Button submitButton) {
        // Add an event handler to the button
        submitButton.setOnAction(event -> {
            // Get the text from the TextField
            this.userInput = textField.getText();

            // Set the node's name to the user input
            node.setName(userInput);

            // Set the label's text to the user input
            node.getNameLabel().setText(userInput);

            // Remove the TextField and Button from the scene
            root.getChildren().removeAll(textField, submitButton);
            resetUserInput();
        });

        // Add the TextField and Button to the Pane
        root.getChildren().addAll(textField, submitButton);
    }

    /**
     * handles the description of a node for editing
     *
     * @param node the node to handle
     * @param textField the text field
     * @param submitButton the submit button
     */
    private void handleDescription(Node node, TextField textField, Button submitButton) {
        // Add an event handler to the button
        submitButton.setOnAction(event -> {
            // Get the text from the TextField
            this.userInput = textField.getText();

            // Set the node's description to the user input
            node.setDescription(userInput);
            // Set the label's text to the user input
            node.getDescriptionLabel().setText(userInput);

            // Remove the TextField and Button from the scene
            root.getChildren().removeAll(textField, submitButton);
            resetUserInput();
        });

        // Add the TextField and Button to the Pane
        root.getChildren().addAll(textField, submitButton);
    }

    /**
     * handles the scoring of a node
     *
     * @param shape the shape to handle the scoring for
     */
    private void handleScoring(Shape shape) {
        Node node = (Node) shape.getUserData();
        NodeType type = node.getType();

        switch (type) {
            // Threat or Top Event
            case TOP_EVENT , THREAT:
                List<String> scores = NodeLabel.scoreThreatLabel();
                String confidentiality = "Confidentiality: " + scores.get(0); String integrity = "Integrity: " + scores.get(1); String availability = "Availability: " + scores.get(2);
                String score = confidentiality + "\n" + integrity + "\n" + availability;
                node.setScore(score);
                node.getScoreLabel().setText(score);
                break;
            // Mitigation, Counter Mitigation, Action
            case MITIGATION, COUNTER_MITIGATION, ACTION:
                List<String> scoresMit = NodeLabel.scoreMitigatorsLabel();
                String effectiveness = "Effectiveness: " + scoresMit.get(0); String efficiency = "Difficulty: " + scoresMit.get(1); String coverage =  "Coverage: " + scoresMit.get(2); String opportunity =  "Opportunity: " + scoresMit.get(3);
                String scoreMit = effectiveness + "\n" + efficiency + "\n" + coverage + "\n" + opportunity;
                node.setScore(scoreMit);
                node.getScoreLabel().setText(scoreMit);
                break;
            // Exposure
            case EXPOSURE:
                List<String> scoresExp = NodeLabel.scoreExpLabel();
                //get strings for: Financial, Operational, Legal, Strategic, Environmental, Health & Safety, Reputation
                String financial = "Financial: " + scoresExp.get(0); String operational = "Operational: " + scoresExp.get(1); String legal = "Legal: " + scoresExp.get(2); String strategic = "Strategic: " + scoresExp.get(3); String environmental = "Environmental: " + scoresExp.get(4); String healthSafety = "Health & Safety: " + scoresExp.get(5); String reputation = "Reputation: " + scoresExp.get(6);
                String scoreExp = financial + "\n" + operational + "\n" + legal + "\n" + strategic + "\n" + environmental + "\n" + healthSafety + "\n" + reputation;
                node.setScore(scoreExp);
                node.getScoreLabel().setText(scoreExp);
                break;
            case VULNERABILITY:
                List<String> scoresVuln = NodeLabel.scoreVulnLabel();
                String attackVector = "Attack Vector: " + scoresVuln.get(0); String attackComplexity = "Attack Complexity: " + scoresVuln.get(1); String privilegesRequired = "Privileges Required: " + scoresVuln.get(2); String userInteraction = "User Interaction: " + scoresVuln.get(3);
                String scoreVuln = attackVector + "\n" + attackComplexity + "\n" + privilegesRequired + "\n" + userInteraction;
                node.setScore(scoreVuln);
                node.getScoreLabel().setText(scoreVuln);
                break;
            default:
                break;
        }
    }

    /**
     *  Loads a node onto the view pane
     *
     * @param type the type of node to load
     * @param x the x coordinate
     * @param y the y coordinate
     * @param id the id of the node
     * @param Name the name of the node
     * @param Description the description of the node
     * @param Score the score of the node
     * @param scaleX the x scale
     * @param scaleY the y scale
     * @return the shape of the node
     *
     * @see project.bowtie.Model.BTmodel.Nodes.NodeType
     * @see project.bowtie.App.Controllers.ViewPane.Obj.Nodes.NodeFactory
     */
    public Shape LoadNode(NodeType type, double x, double y, String id, String Name, String Description, String Score, Double scaleX, Double scaleY) {

        Shape shape = NodeFactory.createNode(type);
        // controllers and listeners
        setNodeListeners(shape);
        Node node = new Node(id, type, Name);

        // Set the shape's position
        shape.setLayoutX(x);
        shape.setLayoutY(y);

        // link pane and node
        shape.setId(node.getId());
        nodeMap.put(node.getId(), node);

        //init labels
        NodeLabel nameLabel = new NodeLabel(Name, shape, NodeDetail.NAME, type);
        NodeLabel infoLabel = new NodeLabel(Description, shape, NodeDetail.DESCRIPTION, type);
        NodeLabel scoreLabel = new NodeLabel(Score, shape, NodeDetail.SCORE, type);
        node.setNameLabel(nameLabel);
        node.setDescriptionLabel(infoLabel);
        node.setScoreLabel(scoreLabel);

        // link shape on viewPane and node
        shape.setUserData(node);
        // update node count
        nodeCount++;

        // Set the shape's scale
        shape.setScaleX(scaleX);
        shape.setScaleY(scaleY);

        // Add the shape to the root
        root.getChildren().addAll(shape, infoLabel, nameLabel, scoreLabel);

        return shape;
    }

    public void updateNodeListeners(Shape shape) {
        // Drag and drop listeners

        // Initial drag offset
        final double[] offsetX = {0};
        final double[] offsetY = {0};

        shape.removeEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, shape.getOnMousePressed());
        shape.removeEventFilter(javafx.scene.input.MouseEvent.MOUSE_DRAGGED, shape.getOnMouseDragged());

        // Set mouse pressed event
        shape.setOnMousePressed(event -> {
            // Calculate the offset from the top-left corner of the rectangle
            // Adjust by the current scale of the root pane to ensure consistency at different zoom levels
            offsetX[0] = (event.getSceneX() - shape.getLayoutX()) / root.getScaleX();
            offsetY[0] = (event.getSceneY() - shape.getLayoutY()) / root.getScaleY();
        });

        // Set mouse dragged event
        shape.setOnMouseDragged(event -> {
            // Update the position of the rectangle
            // Consider the current scale of the root pane to match the drag distance with mouse movement
            shape.setLayoutX((event.getSceneX() - offsetX[0] * root.getScaleX()));
            shape.setLayoutY((event.getSceneY() - offsetY[0] * root.getScaleY()));
        });

        // The rest of your listener setup remains the same
    }
}
