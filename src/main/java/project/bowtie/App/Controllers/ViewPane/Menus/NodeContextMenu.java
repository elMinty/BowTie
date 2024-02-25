package project.bowtie.App.Controllers.ViewPane.Menus;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.shape.Shape;
import project.bowtie.App.Controllers.ViewPane.NodeController;
import project.bowtie.App.Controllers.ViewPane.Obj.Node_Detail;

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
        MenuItem editQuantifier = new MenuItem("Edit Type");

        MenuItem viewName = new MenuItem("View Name");
        MenuItem viewDescription = new MenuItem("View Description");
        MenuItem viewType = new MenuItem("View Type");


        // handlers using handleEdit(shape, detail) and handleView(shape, detail)

        editName.setOnAction(e -> nc.handleEdit(shape, Node_Detail.NAME));
        editDescription.setOnAction(e -> nc.handleEdit(shape, Node_Detail.DESCRIPTION));
        editQuantifier.setOnAction(e -> nc.handleEdit(shape, Node_Detail.QUANTIFIER));

        viewName.setOnAction(e -> nc.handleView(shape, Node_Detail.NAME));
        viewDescription.setOnAction(e -> nc.handleView(shape, Node_Detail.DESCRIPTION));
        viewType.setOnAction(e -> nc.handleView(shape, Node_Detail.QUANTIFIER));

        editDetails.getItems().addAll(editName, editDescription, editQuantifier);

        viewDetails.getItems().addAll(viewName, viewDescription, viewType);

        detailsMenu.getItems().addAll(editDetails, viewDetails);

        contextMenu.getItems().add(detailsMenu);




    }

}