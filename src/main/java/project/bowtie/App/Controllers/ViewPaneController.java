package project.bowtie.App.Controllers;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;

//import project.bowtie.App.Controllers.ViewPane.ShapeController;
import javafx.stage.Screen;
import javafx.stage.Stage;
import project.bowtie.App.Controllers.ViewPane.ShapeController;
import project.bowtie.App.Controllers.ViewPane.Menus.ViewPaneContextMenu;
import project.bowtie.BTApp;
import project.bowtie.BTController;
import project.bowtie.Model.BTmodel.Bowtie.Bowtie;
import project.bowtie.Model.BTmodel.Nodes.Node;
import project.bowtie.Model.BTmodel.Nodes.NodeType;

public class ViewPaneController{


    @FXML
    public AnchorPane root; // establish the root of the view pane

    private final ViewPaneContextMenu contextMenuBuilder = new ViewPaneContextMenu(); //establish the context menu
    private ContextMenu contextMenu;
    public ShapeController sc;

    private Node topEvent;
    private Bowtie bowtie;

    private Scene scene;
    private Stage stage;

    @FXML


    public void initViewPane(Scene scene, Stage stage){
        this.scene = scene;
        this.stage = stage;
        sc = new ShapeController(root);;
        setContextMenu();
        setTopEvent();


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

    //set up the top event
    private void setTopEvent() {

        sc.handleAddNode(NodeType.TOP_EVENT, 900, 360);
        topEvent = new Node("0", NodeType.TOP_EVENT, "Top Event");
        bowtie = new Bowtie(topEvent);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

}

