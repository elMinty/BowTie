package project.bowtie.App.Controllers.ViewPane.Menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import project.bowtie.App.Controllers.ViewPane.ShapeController;
import project.bowtie.Model.BTmodel.Nodes.NodeType;

public class AddMenu {

    double x, y;
    ShapeController sc;

    private Menu addMenu = new Menu("Add");

    public Menu initAddMenu(ShapeController sc) {
        this.sc = sc;
        // Add the Node submenu directly
        Menu nodeMenu = new NodeMenu().initNodeMenu();
        addMenu.getItems().add(nodeMenu);

        return addMenu;
    }

    public void setCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    private class NodeMenu {

        public Menu initNodeMenu() {
            // Menu items
            Menu nodeMenu = new Menu("Node");
            MenuItem topEvent = new MenuItem("Top Event");
            MenuItem exposure = new MenuItem("Exposure");
            MenuItem threat = new MenuItem("Threat");
            MenuItem mitigation = new MenuItem("Mitigation");
            MenuItem counterMitigation = new MenuItem("Counter Mitigation");
            MenuItem action = new MenuItem("Action");
            MenuItem vulnerability = new MenuItem("Vulnerability");

            // Event handlers for adding nodes
            topEvent.setOnAction(event -> sc.handleAddNode(NodeType.TOP_EVENT,x, y));
            exposure.setOnAction(event -> sc.handleAddNode(NodeType.EXPOSURE,x, y));
            threat.setOnAction(event -> sc.handleAddNode(NodeType.THREAT,x, y));
            mitigation.setOnAction(event -> sc.handleAddNode(NodeType.MITIGATION,x, y));
            counterMitigation.setOnAction(event -> sc.handleAddNode(NodeType.COUNTER_MITIGATION,x, y));
            action.setOnAction(event -> sc.handleAddNode(NodeType.ACTION,x, y));
            vulnerability.setOnAction(event -> sc.handleAddNode(NodeType.VULNERABILITY,x, y));

            nodeMenu.getItems().addAll(
                    topEvent,
                    exposure,
                    threat,
                    mitigation,
                    counterMitigation,
                    action,
                    vulnerability
            );

            return nodeMenu;
        }
    }
}
