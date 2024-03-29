package BowTie.Controllers;

import BowTie.Controllers.ViewPane.NodeController;
import BowTie.Model.Bowtie.Bowtie;
import BowTie.Model.Nodes.Node;
import BowTie.Model.Nodes.NodeType;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import BowTie.Controllers.ViewPane.Menus.ViewPaneContextMenu;

/**
 * Controller for the view pane - Initiates Bowtie Model and handles context menu
 */
public class ViewPaneController{
    @FXML
    public AnchorPane root; // establish the root of the view pane
    private final ViewPaneContextMenu contextMenuBuilder = new ViewPaneContextMenu(); //establish the context menu
    private ContextMenu contextMenu;
    public NodeController nc;
    private Node topEvent;
    public Bowtie bowtie;
    public double scale = 1.0;
    private Scene scene;
    private Stage stage;


    /**
     * Initializes the view pane
     * @param scene the scene
     * @param stage the stage
     */
    @FXML
    public void initViewPane(Scene scene, Stage stage, PathPaneController pathPaneController){
        this.scene = scene;
        this.stage = stage;
        this.setColor(Color.BLANCHEDALMOND);
        nc = new NodeController(root, pathPaneController);;
        setContextMenu();
        addTopEvent();
    }

    /**
     * Initializes the Context Menu for the view pane
     * Adds Context Menu handlers for the view pane - adds positions for the context menu
     */
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

    /**
     * Adds the top event to the view pane
     * Sets to set position of the top event
     */
    private void addTopEvent() {
        String id = nc.handleAddNode(NodeType.TOP_EVENT, 500, 100);
        topEvent = nc.getNode(id);
        bowtie = new Bowtie(topEvent);
    }

    /**
     * Sets the top event
     * @param topEvent the top event
     */
    public void setTopEvent(Node topEvent){
        this.topEvent = topEvent;
        bowtie = new Bowtie(topEvent);
    }

    /**
     * Get the top event
     * @return the top event
     */
    public Node getTopEvent() {
        return topEvent;
    }

    /**
     * Sets the scene
     * @param scene
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Sets the Color of the view pane
     * @param color Color of the view pane
     */
    public void setColor(Color color){
        root.setStyle("-fx-background-color: " + "'" + color + "'" + ";");
    }

    /**
     * Handles the zoom action - sets scale
     * @param scale the scale
     */
    public void setScale(double scale) {
        this.scale = scale;
    }
}


