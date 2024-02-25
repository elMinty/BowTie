package project.bowtie.App.Controllers.ViewPane;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import project.bowtie.App.Controllers.DragController;
import project.bowtie.App.Controllers.ViewPane.Menus.NodeContextMenu;
import project.bowtie.App.Controllers.ViewPane.Obj.Connector;
import project.bowtie.App.Controllers.ViewPane.Obj.NodeFactory;
import project.bowtie.Model.BTmodel.Nodes.Node;
import project.bowtie.Model.BTmodel.Nodes.NodeType;
import project.bowtie.Model.BTmodel.Nodes.NodeUtils;
import project.bowtie.App.Controllers.ViewPane.Obj.Node_Detail;

import java.util.HashMap;
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
                nodeMap.remove(node.getId());
                NodeUtils.delete(node);
                root.getChildren().remove(shape);
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



    public void handleAddNode(NodeType type, double x, double y) {

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
        shape.setUserData(node);
        nodeMap.put(node.getId(), node);

        Label nameLabel = setLabelName(shape, node.getName());
        Label infoLabel = setInfoLabel(shape, node.getDescription());

        shape.setAccessibleText(node.getName());
        shape.setAccessibleRoleDescription(node.getDescription());
        // update node count
        nodeCount++;

        // Add the shape to the root
        root.getChildren().addAll(shape, infoLabel, nameLabel);

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

    private Label setInfoLabel(Shape shape, String label) {
        Label infoLabel = new Label();
        // Setup the information label (but don't add it to the scene yet)
        infoLabel.setText(label);
        infoLabel.setVisible(false); // Initially invisible

        shape.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            // Position the label based on the shape's position
            infoLabel.setLayoutX(shape.getLayoutX() + 30); // Offset by 10px for example
            infoLabel.setLayoutY(shape.getLayoutY() + 30);
            infoLabel.setVisible(true);


            // Add the label to the scene or a parent container if not already done
        });

        shape.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            infoLabel.setVisible(false);
        });

        // Handler for dragging the node
        shape.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            // Update the label position as the node is dragged
            // You might need to adjust the offset to fit your specific needs
            infoLabel.setLayoutX(shape.getLayoutX() + 30); // Offset to position the label correctly
            infoLabel.setLayoutY(shape.getLayoutY() + 30);
        });


        return infoLabel;

    }

    public Label setLabelName(Shape shape, String name) {

        // Sets the nodes name - displays the name as text based on the shape's position
        // create label that displays the name of the node on the shape position

        Node node = (Node) shape.getUserData();
        node.setName(name);

        Label nameLabel = new Label();
        // Setup the information label (but don't add it to the scene yet)
        nameLabel.setText(name);

        return nameLabel;

    }

    public void handleEdit(Shape shape, Node_Detail detail) {

        // gets a window popup for entering a string to edit the node's name, description, or quantifier


        switch (detail) {
            case NAME:
                getUserText();
                setLabelName(shape, userInput);
                resetUserInput();
                break;
            case DESCRIPTION:

                break;
            case QUANTIFIER:

                break;
            default:
                break;
        }
    }

    public void handleView(Shape shape, Node_Detail detail) {


        switch (detail) {
            case NAME:
                break;
            case DESCRIPTION:

                break;
            case QUANTIFIER:

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

    public void getUserText() {


        // Create a TextField for user input
        TextField textField = new TextField();
        textField.setPromptText("Enter your text here");
        textField.setLayoutX(100); // Set X position
        textField.setLayoutY(100); // Set Y position

        // Create a Button to submit the input
        Button submitButton = new Button("Submit");
        submitButton.setLayoutX(250); // Set X position
        submitButton.setLayoutY(100); // Set Y position

        // Add an event handler to the button
        submitButton.setOnAction(event -> {
            // Get the text from the TextField
            userInput = textField.getText();

            // Remove the TextField and Button from the scene
            root.getChildren().removeAll(textField, submitButton);
        });

        // Add the TextField and Button to the Pane
        root.getChildren().addAll(textField, submitButton);

    }


}
