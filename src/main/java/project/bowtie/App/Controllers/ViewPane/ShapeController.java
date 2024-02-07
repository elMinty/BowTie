package project.bowtie.App.Controllers.ViewPane;

import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import project.bowtie.App.Controllers.DragController;
import project.bowtie.App.Controllers.ViewPane.Menus.ShapeContextMenu;
import project.bowtie.App.Controllers.ViewPane.Obj.NodeFactory;
import project.bowtie.Model.BTmodel.Nodes.NodeType;

import java.util.Optional;

public class ShapeController{

    Pane root;
    NodeFactory nf = new NodeFactory();
    ShapeContextMenu shapeContextMenu;

    public ShapeController(AnchorPane root) {
        this.root = root;
        shapeContextMenu = new ShapeContextMenu(this);
    }

    public void handleDelete(Shape shape) {
        root.getChildren().remove(shape);
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
        DragController dragController = new DragController(shape, true);

        shape.setOnContextMenuRequested(event -> {
            shapeContextMenu.showContextMenu(shape, event.getScreenX(), event.getScreenY());
            event.consume();  // Add this line to consume the event
        });

        Label infoLabel = setLabel(shape, "Info about the node");



        shape.setLayoutX(x);
        shape.setLayoutY(y);
        root.getChildren().addAll(shape, infoLabel);

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
}
