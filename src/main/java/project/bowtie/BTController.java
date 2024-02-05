package project.bowtie;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import project.bowtie.App.Controllers.MenuBarController;
import project.bowtie.App.Controllers.ViewPaneController;


public class BTController {

    public AnchorPane viewPane;
    public MenuBar menuBar;

    @FXML private MenuBarController menuBarController;

    @FXML private ViewPaneController viewPaneController;


}
