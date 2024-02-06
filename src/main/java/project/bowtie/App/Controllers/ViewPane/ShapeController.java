package project.bowtie.App.Controllers.ViewPane;

import javafx.scene.control.TextInputDialog;
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


        shape.setLayoutX(x);
        shape.setLayoutY(y);
        root.getChildren().add(shape);

    }


}
