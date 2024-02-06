package project.bowtie.App.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;

//import project.bowtie.App.Controllers.ViewPane.ShapeController;
import project.bowtie.App.Controllers.ViewPane.ShapeController;
import project.bowtie.App.Controllers.ViewPane.Menus.ViewPaneContextMenu;

public class ViewPaneController {


    @FXML
    public AnchorPane root; // establish the root of the view pane

    private final ViewPaneContextMenu contextMenuBuilder = new ViewPaneContextMenu(); //establish the context menu
    private ContextMenu contextMenu;
    public ShapeController sc;

    @FXML


    public void initialize() {
        sc = new ShapeController(root);;
        setContextMenu();

    }

    private void setContextMenu() {

        // Initialize the context menu
        contextMenu = contextMenuBuilder.initContextMenu(sc);

        // Set event handler for context menu request
        root.setOnContextMenuRequested(event -> {
            contextMenuBuilder.setCoordinates(event.getSceneX(), event.getSceneY());
            contextMenu.show(root, event.getSceneX(), event.getSceneY());
        });

        // Set up left-click event listener to hide the context menu
        root.setOnMouseClicked(event -> {
            if (contextMenu != null && contextMenu.isShowing()) {
                contextMenu.hide();
            }
        });

    }

}


