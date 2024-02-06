package project.bowtie.App.Controllers.ViewPane.Menus;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import project.bowtie.App.Controllers.ViewPane.ShapeController;
import project.bowtie.App.Controllers.ViewPaneController;

import java.util.Optional;

public class ShapeContextMenu {

    private ContextMenu contextMenu;
    private Shape shape;
    ShapeController sc;

    public ShapeContextMenu(ShapeController sc) {

        contextMenu = new ContextMenu();

        // Menu item for delete
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> sc.handleDelete(shape));

        // Menu item for scale
        MenuItem scaleItem = new MenuItem("Scale");
        scaleItem.setOnAction(event -> sc.handleScale(this.shape));

        contextMenu.getItems().addAll(deleteItem, scaleItem);
    }

    public void showContextMenu(Shape shape, double x, double y) {
        this.shape = shape;
        contextMenu.show(shape, x, y);
    }

}