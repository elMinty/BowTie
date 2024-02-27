package project.bowtie;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.bowtie.App.Controllers.MenuBarController;
import project.bowtie.App.Controllers.ViewPaneController;


public class BTController{

    public AnchorPane viewPane;
    public MenuBar menuBar;
    private Scene scene;

    @FXML private ScrollPane scrollPane;

    @FXML private MenuBarController menuBarController;

    @FXML private ViewPaneController viewPaneController;

    // This method will be explicitly called from BTApp after the scene is set
    public void initController(Scene scene, Stage stage) {
        this.scene = scene;
        if (viewPaneController != null) {
            viewPaneController.setScrollPane(scrollPane);
            viewPaneController.initViewPane(scene, stage);

        }
        if (menuBarController != null) {
            menuBarController.initMenuBar(scene, stage);
            menuBarController.setViewPaneRoot(viewPaneController.root);
            menuBarController.setNodeController(viewPaneController.nc);
            menuBarController.setVPC(viewPaneController);
        }
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }


}
