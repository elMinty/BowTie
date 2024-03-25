package project.bowtie;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.bowtie.App.Controllers.MenuBarController;
import project.bowtie.App.Controllers.ViewPaneController;


/**
 * JavaFX App
 *
 * This class is the entry point for the application. It loads the main.fxml file and sets up the stage.
 * Sets the controller as BTController and initializes it.
 *
 */
public class BTController{

    public AnchorPane viewPane;
    public MenuBar menuBar;
    private Scene scene;


    // FXML elements
    @FXML private MenuBarController menuBarController;
    @FXML private ViewPaneController viewPaneController;

    /**
     * Start method for the application
     * Initiates Controller which sets for the MenuBarController and ViewPaneController
     *
     * @param stage app stage
     * @param scene app scene
     *
     */
    public void initController(Scene scene, Stage stage) {
        this.scene = scene;
        if (viewPaneController != null) {
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
