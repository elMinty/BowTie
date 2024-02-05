package project.bowtie.App.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

public class ViewPaneController {

    @FXML
    public AnchorPane root;


    // Event handler for Option 1
    @FXML
    private void handleOption1(ActionEvent event) {
        System.out.println("Option 1 selected");
        // Add your logic here
    }

    // Event handler for Option 2
    @FXML
    private void handleOption2(ActionEvent event) {
        System.out.println("Option 2 selected");
        // Add your logic here
    }

    @FXML
    public void initialize() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Option 1");
        item1.setOnAction(this::handleOption1);
        contextMenu.getItems().add(item1);

        MenuItem item2 = new MenuItem("Option 2");
        item2.setOnAction(this::handleOption2);
        contextMenu.getItems().add(item2);

        // Attach context menu to AnchorPane
        root.setOnContextMenuRequested(event -> contextMenu.show(root, event.getScreenX(), event.getScreenY()));
    }

}
