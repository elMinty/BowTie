package project.bowtie.App.Controllers.ViewPane.Menus;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.shape.Shape;
import project.bowtie.App.Controllers.ViewPane.NodeController;
import project.bowtie.Model.BTmodel.Nodes.NodeDetail;

public class NodeContextMenu {

    private ContextMenu contextMenu;
    private Shape shape;
    NodeController nc;


    public NodeContextMenu(NodeController nc) {

        this.nc = nc;

        contextMenu = new ContextMenu();

        // Menu item for delete
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> nc.handleDelete(shape));

        // Menu item for scale
        MenuItem scaleItem = new MenuItem("Scale");
        scaleItem.setOnAction(event -> nc.handleScale(this.shape));

        // Connect menu with options before and after
        Menu ConnectMenu = new Menu("Connect");

        contextMenu.getItems().addAll(deleteItem, scaleItem);

        initDetailsMenu();

        initConnectMenu();
    }

    public void showContextMenu(Shape shape, double x, double y) {
        this.shape = shape;
        contextMenu.show(shape, x, y);
    }

    private Menu initConnectMenu() {
        // Menu items
        Menu connectMenu = new Menu("Connect");
        MenuItem connectBefore = new MenuItem("Connect Before");
        MenuItem connectAfter = new MenuItem("Connect After");
        MenuItem disconnect = new MenuItem("Disconnect");


        connectAfter.setOnAction(e -> {
            nc.connector.setConnectMode(true);
            String sourceNodeId = shape.getId(); // currentNode represents the node that opened the context menu
            // Additional actions to indicate the application is in connection mode, if necessary
            nc.connector.setConnectType(ConnectionMode.AFTER);
            nc.connector.setSourceNodeId(sourceNodeId);
            nc.connector.setSourceShape(shape);
        });

        connectBefore.setOnAction(e -> {
            nc.connector.setConnectMode(true);
            String sourceNodeId = shape.getId(); // currentNode represents the node that opened the context menu
            // Additional actions to indicate the application is in connection mode, if necessary
            nc.connector.setConnectType(ConnectionMode.BEFORE);
            nc.connector.setSourceNodeId(sourceNodeId);
            nc.connector.setSourceShape(shape);
        });

        disconnect.setOnAction(e -> {
            nc.connector.setConnectMode(true);
            String sourceNodeId = shape.getId(); // currentNode represents the node that opened the context menu
            // Additional actions to indicate the application is in connection mode, if necessary
            nc.connector.setConnectType(ConnectionMode.DISCONNECT);
            nc.connector.setSourceNodeId(sourceNodeId);
            nc.connector.setSourceShape(shape);
        });

        connectMenu.getItems().addAll(connectBefore, connectAfter, disconnect);
        contextMenu.getItems().add(connectMenu);

        return connectMenu;
    }

    public void initDetailsMenu() {
        // Menu items
        Menu detailsMenu = new Menu("Details");
        Menu editDetails = new Menu("Edit Details");
        Menu viewDetails = new Menu("View Details");

        // Menu items for edit and view
        MenuItem editName = new MenuItem("Edit Name");
        MenuItem editDescription = new MenuItem("Edit Description");
        MenuItem editScore = new MenuItem("Edit Score");

        Menu viewName = initViewName();
        Menu viewDescription = initViewDescription();
        Menu viewScore = initViewScore();




        editDetails.getItems().addAll(editName, editDescription, editScore);

        viewDetails.getItems().addAll(viewName, viewDescription, viewScore);

        detailsMenu.getItems().addAll(editDetails, viewDetails);

        contextMenu.getItems().add(detailsMenu);


    }

    private Menu initViewName(){
        Menu viewName = new Menu("View Name");

        MenuItem NAlways = new MenuItem("Always");
        MenuItem NOnHover = new MenuItem("On Hover");
        MenuItem NHidden = new MenuItem("Hidden");

        viewName.getItems().addAll(NAlways, NOnHover, NHidden);

        NAlways.setOnAction(e -> nc.handleView(shape, NodeDetail.NAME, ViewOption.ALWAYS));
        NOnHover.setOnAction(e -> nc.handleView(shape, NodeDetail.NAME, ViewOption.ON_HOVER));
        NHidden.setOnAction(e -> nc.handleView(shape, NodeDetail.NAME, ViewOption.NEVER));

        return viewName;

    }

    private Menu initViewDescription(){

        Menu viewDescription = new Menu("View Description");

        MenuItem DAlways = new MenuItem("Always");
        MenuItem DOnHover = new MenuItem("On Hover");
        MenuItem DHidden = new MenuItem("Hidden");

        viewDescription.getItems().addAll(DAlways, DOnHover, DHidden);

        DAlways.setOnAction(e -> nc.handleView(shape, NodeDetail.DESCRIPTION, ViewOption.ALWAYS));
        DOnHover.setOnAction(e -> nc.handleView(shape, NodeDetail.DESCRIPTION, ViewOption.ON_HOVER));
        DHidden.setOnAction(e -> nc.handleView(shape, NodeDetail.DESCRIPTION, ViewOption.NEVER));

        return viewDescription;

    }

    private Menu initViewScore(){

        Menu viewScore = new Menu("View Score");

        MenuItem SAlways = new MenuItem("Always");
        MenuItem SOnHover = new MenuItem("On Hover");
        MenuItem SHidden = new MenuItem("Hidden");

        viewScore.getItems().addAll(SAlways, SOnHover, SHidden);

        SAlways.setOnAction(e -> nc.handleView(shape, NodeDetail.SCORE, ViewOption.ALWAYS));
        SOnHover.setOnAction(e -> nc.handleView(shape, NodeDetail.SCORE, ViewOption.ON_HOVER));
        SHidden.setOnAction(e -> nc.handleView(shape, NodeDetail.SCORE, ViewOption.NEVER));

        return viewScore;

    }

}