package project.bowtie;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.bowtie.App.Controllers.MenuBarController;
import project.bowtie.App.Controllers.PathPane.PathPaneController;
import project.bowtie.App.Controllers.ViewPaneController;


/**
 * BTController class
 * This class is the main controller for the application
 * It initiates the MenuBarController and ViewPaneController
 * sets the viewPane root to the MenuBarController
 * sets the NodeController to the MenuBarController
 * sets the ViewPaneController to the MenuBarController
 */
public class BTController{

    public AnchorPane viewPane;
    public MenuBar menuBar;
    public SplitPane pathPane;
    private Scene scene;


    // FXML elements
    @FXML private MenuBarController menuBarController;
    @FXML private ViewPaneController viewPaneController;
    @FXML private PathPaneController pathPaneController;


    @FXML private ScrollPane scrollPane;

    /**
     * Start method for the application
     * Initiates Controller which sets for the MenuBarController and ViewPaneController
     *
     * @param stage app stage
     * @param scene app scene
     */
    public void initController(Scene scene, Stage stage) {
        this.scene = scene;
        if (viewPaneController != null ) {
            pathPaneController.initialize();
            viewPaneController.initViewPane(scene, stage, pathPaneController);
        }
        if (menuBarController != null) {
            menuBarController.initMenuBar(scene, stage, pathPaneController);
            menuBarController.setViewPaneRoot(viewPaneController.root, scrollPane);
            menuBarController.setNodeController(viewPaneController.nc);
            menuBarController.setVPC(viewPaneController);
        }
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
