package project.bowtie.App.Controllers.ViewPane.Menus;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import project.bowtie.App.Controllers.ViewPane.ShapeController;

public class ViewPaneContextMenu{


    double x, y;
    ShapeController sc;


    AddMenu addMenu = new AddMenu();

    // Factory method to create the main context menu
    public ContextMenu initContextMenu(ShapeController sc) {

        this.sc = sc;
        ContextMenu contextMenu = new ContextMenu();
        // Add the 'Add' submenu
        MenuItem am = addMenu.initAddMenu(sc);
        contextMenu.getItems().add(am);

        return contextMenu;
    }

    public void setCoordinates(double x, double y) {
        this.x = x;
        this.y = y;

        addMenu.setCoordinates(x, y);
    }

}
