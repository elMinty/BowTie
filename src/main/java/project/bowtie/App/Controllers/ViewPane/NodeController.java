package project.bowtie.App.Controllers.ViewPane;

import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import project.bowtie.App.Controllers.DragController;
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

public class NodeController {

    Pane root;
    NodeFactory nf = new NodeFactory();
    NodeContextMenu nodeContextMenu;

    // Map to hold Pane -> Data associations
    private Map<String, Node> nodeMap = new HashMap<String, Node>();
    private int nodeCount = 0;

    public Connector connector;

    private String userInput = "";


    public NodeController(AnchorPane root) {
        this.root = root;

        nodeContextMenu = new NodeContextMenu(this);
        connector = new Connector(nodeMap);
    }

    public void handleDelete(Shape shape) {
        if (shape.getUserData() != null) {
            Node node = (Node) shape.getUserData();
            if (node.getType() == NodeType.TOP_EVENT) {
                // keep the top event and show a warning
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("TopEvent");
                alert.setContentText("Top Event cannot be deleted.");

                alert.showAndWait();
            }

            else {
                // Otherwise, remove it from the node map

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
                // Handle invalid input
            }
        });
    }



    public Shape handleAddNode(NodeType type, double x, double y) {

        Shape shape = NodeFactory.createNode(type);

        // controllers and listeners
        DragController dragController = new DragController(shape, true);
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

        shape.setUserData(node);
        // update node count
        nodeCount++;

        // Add the shape to the root
        root.getChildren().addAll(shape, infoLabel, nameLabel, scoreLabel);

        return shape;

    }

    private void setNodeListeners(Shape shape) {
        shape.setOnContextMenuRequested(event -> {
            nodeContextMenu.showContextMenu(shape, event.getScreenX(), event.getScreenY());
            event.consume();  // Add this line to consume the event
        });

        // Check if connect mode is enabled
        shape.setOnMouseClicked(event -> {
            if (connector.getConnectMode()) {
                String targetNodeId = shape.getId(); // currentNode represents the node that opened the context menu
                connector.setTargetNodeId(targetNodeId);
                connector.flush(shape);
            }
        });
    }

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
                // Add an event handler to the button
                submitButton.setOnAction(event -> {
                    // Get the text from the TextField
                    this.userInput = textField.getText();
                    System.out.println("User input: " + userInput);

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

                break;
            case DESCRIPTION:
                // Add an event handler to the button
                submitButton.setOnAction(event -> {
                    // Get the text from the TextField
                    this.userInput = textField.getText();
                    System.out.println("User input: " + userInput);

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

                break;
            case SCORE:

                // score handling
                handleScoring(shape);

                break;
            default:
                break;
        }



    }

    public void handleView(Shape shape, NodeDetail detail, ViewOption viewOption) {

        System.out.println("View Option: " + viewOption + " for " + detail + " on " + shape.getId() + " NodeDetail: " + detail);


        switch (detail) {
            case NAME:
                NodeLabel nameLabel = ((Node) shape.getUserData()).getNameLabel();
                System.out.println("Name Label: " + nameLabel.getText());
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


    public Pane getRoot() {
        return root;
    }

    public Node getNode(String id) {
        return nodeMap.get(id);
    }

    private void resetUserInput() {
        userInput = "";
    }

    private void handleScoring(Shape shape) {
        Node node = (Node) shape.getUserData();

        NodeType type = node.getType();

        switch (type) {
            //Threat or Top Event
            case TOP_EVENT , THREAT:
                List<String> scores = NodeLabel.scoreThreatLabel();
                String confidentiality = "Confidentiality: " + scores.get(0); String integrity = "Integrity: " + scores.get(1); String availability = "Availability: " + scores.get(2);

                String score = confidentiality + "\n" + integrity + "\n" + availability;

                node.setScore(score);
                node.getScoreLabel().setText(score);

                System.out.println("Score Label: " + node.getScoreLabel().getText());

                break;
            case MITIGATION, COUNTER_MITIGATION, ACTION:
                List<String> scoresMit = NodeLabel.scoreMitigatorsLabel();
                String effectiveness = "Effectiveness: " + scoresMit.get(0); String efficiency = "Difficulty: " + scoresMit.get(1);

                String scoreMit = effectiveness + "\n" + efficiency;

                node.setScore(scoreMit);
                node.getScoreLabel().setText(scoreMit);

                System.out.println("Score Label: " + node.getScoreLabel().getText());


                break;
            case EXPOSURE:
                List<String> scoresExp = NodeLabel.scoreExpLabel();

                //get strings for: Financial, Operational, Legal, Strategic, Environmental, Health & Safety, Reputation

                String financial = "Financial: " + scoresExp.get(0); String operational = "Operational: " + scoresExp.get(1); String legal = "Legal: " + scoresExp.get(2); String strategic = "Strategic: " + scoresExp.get(3); String environmental = "Environmental: " + scoresExp.get(4); String healthSafety = "Health & Safety: " + scoresExp.get(5); String reputation = "Reputation: " + scoresExp.get(6);

                String scoreExp = financial + "\n" + operational + "\n" + legal + "\n" + strategic + "\n" + environmental + "\n" + healthSafety + "\n" + reputation;

                node.setScore(scoreExp);
                node.getScoreLabel().setText(scoreExp);

                System.out.println("Score Label: " + node.getScoreLabel().getText());

                break;
            case VULNERABILITY:
                List<String> scoresVuln = NodeLabel.scoreVulnLabel();
                String attackVector = "Attack Vector: " + scoresVuln.get(0); String attackComplexity = "Attack Complexity: " + scoresVuln.get(1); String privilegesRequired = "Privileges Required: " + scoresVuln.get(2); String userInteraction = "User Interaction: " + scoresVuln.get(3);

                String scoreVuln = attackVector + "\n" + attackComplexity + "\n" + privilegesRequired + "\n" + userInteraction;

                node.setScore(scoreVuln);
                node.getScoreLabel().setText(scoreVuln);

                System.out.println("Score Label: " + node.getScoreLabel().getText());

                break;
            default:
                break;
        }
    }

    public Shape LoadNode(NodeType type, double x, double y, String id, String Name, String Description, String Score, Double scaleX, Double scaleY) {

        Shape shape = NodeFactory.createNode(type);

        // controllers and listeners
        DragController dragController = new DragController(shape, true);
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

        shape.setUserData(node);
        // update node count
        nodeCount++;

        shape.setScaleX(scaleX);
        shape.setScaleY(scaleY);

        // Add the shape to the root
        root.getChildren().addAll(shape, infoLabel, nameLabel, scoreLabel);

        return shape;

    }


}
