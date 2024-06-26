package BowTie.Controllers.ViewPane.Menus;

import BowTie.Controllers.ViewPane.NodeController;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class ViewPaneContextMenu{


    double x, y;
    NodeController sc;


    AddMenu addMenu = new AddMenu();

    // Factory method to create the main context menu
    public ContextMenu initContextMenu(NodeController sc) {

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
