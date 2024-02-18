package project.bowtie.App.Controllers.ViewPane;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import project.bowtie.App.Controllers.DragController;
import project.bowtie.App.Controllers.ViewPane.Menus.ConnectionMode;
import project.bowtie.App.Controllers.ViewPane.Menus.ShapeContextMenu;
import project.bowtie.App.Controllers.ViewPane.Obj.Connector;
import project.bowtie.App.Controllers.ViewPane.Obj.NodeFactory;
import project.bowtie.Model.BTmodel.Nodes.Node;
import project.bowtie.Model.BTmodel.Nodes.NodeType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NodeController {

    Pane root;
    NodeFactory nf = new NodeFactory();
    ShapeContextMenu shapeContextMenu;

    // Map to hold Pane -> Data associations
    private Map<String, Node> nodeMap = new HashMap<String, Node>();
    private int nodeCount = 0;

    public Connector connector;


    public NodeController(AnchorPane root) {
        this.root = root;

        shapeContextMenu = new ShapeContextMenu(this);
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
                nodeMap.remove(node.getId());
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


        Label infoLabel = setLabel(shape, "Top Event: Client Data Leak");
        Node node = new Node(String.valueOf(nodeCount), type, type.toString() + nodeCount);

        // Set the shape's position
        shape.setLayoutX(x);
        shape.setLayoutY(y);

        // link pane and node
        shape.setId(node.getId());
        shape.setUserData(node);
        nodeMap.put(node.getId(), node);

        // update node count
        nodeCount++;

        // Add the shape to the root
        root.getChildren().addAll(shape, infoLabel);

    }

    private void setNodeListeners(Shape shape) {
        shape.setOnContextMenuRequested(event -> {
            shapeContextMenu.showContextMenu(shape, event.getScreenX(), event.getScreenY());
            event.consume();  // Add this line to consume the event
        });

        // Check if connect mode is enabled
        shape.setOnMouseClicked(event -> {
            if (connector.getConnectMode()) {
                String targetNodeId = shape.getId(); // currentNode represents the node that opened the context menu
                System.out.println("Source Node ID: " + targetNodeId);
                connector.setTargetNodeId(targetNodeId);
                connector.flush();
            }
        });
    }

    private Label setLabel(Shape shape, String label) {
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

    public Pane getRoot() {
        return root;
    }

    public Node getNode(String id) {
        return nodeMap.get(id);
    }
}
