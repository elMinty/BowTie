package project.bowtie.App.Controllers.ViewPane.Menus;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.shape.Shape;
import project.bowtie.App.Controllers.ViewPane.NodeController;

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

}