package project.bowtie.App.Controllers;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;

//import project.bowtie.App.Controllers.ViewPane.NodeController;
import javafx.stage.Stage;
import project.bowtie.App.Controllers.ViewPane.NodeController;
import project.bowtie.App.Controllers.ViewPane.Menus.ViewPaneContextMenu;
import project.bowtie.Model.BTmodel.Bowtie.Bowtie;
import project.bowtie.Model.BTmodel.Nodes.Node;
import project.bowtie.Model.BTmodel.Nodes.NodeType;

import javax.swing.event.ChangeListener;

public class ViewPaneController{


    @FXML
    public AnchorPane root; // establish the root of the view pane

    private final ViewPaneContextMenu contextMenuBuilder = new ViewPaneContextMenu(); //establish the context menu
    private ContextMenu contextMenu;
    public NodeController nc;

    private Node topEvent;
    public Bowtie bowtie;

    private Scene scene;
    private Stage stage;


    private double deltaX;
    private double deltaY;

    @FXML


    public void initViewPane(Scene scene, Stage stage){
        this.scene = scene;
        this.stage = stage;

        //make background warm darker beige
        root.setStyle("-fx-background-color: #ceb09a;");
        nc = new NodeController(root);;
        setContextMenu();
        addTopEvent();

    }

    private void setContextMenu() {

        // Initialize the context menu
        contextMenu = contextMenuBuilder.initContextMenu(nc);

        // Set event handler for context menu request
        root.setOnContextMenuRequested(event -> {
            contextMenuBuilder.setCoordinates(event.getX(), event.getY());
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
    private void addTopEvent() {

        nc.handleAddNode(NodeType.TOP_EVENT, 900, 360);
        topEvent = nc.getNode("0");
        bowtie = new Bowtie(topEvent);
    }

    public void setTopEvent(Node topEvent){
        //print the top event
        System.out.println("Top Event setting: " + topEvent.getId());
        this.topEvent = topEvent;
        bowtie = new Bowtie(topEvent);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

}


