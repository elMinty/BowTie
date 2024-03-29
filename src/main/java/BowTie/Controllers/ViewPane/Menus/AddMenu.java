package BowTie.Controllers.ViewPane.Menus;

import BowTie.Controllers.ViewPane.NodeController;
import BowTie.Model.Nodes.NodeType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;


public class AddMenu {


    double x, y;
    NodeController nc;

    private Menu addMenu = new Menu("Add");

    public Menu initAddMenu(NodeController sc) {
        this.nc = sc;
        // Add the Node submenu directly
        initNodeMenu();
        //initTextBox();


        return addMenu;
    }

    public void setCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void initNodeMenu() {
        // Menu items
        Menu nodeMenu = new Menu("Node");
        //MenuItem topEvent = new MenuItem("Top Event");
        MenuItem exposure = new MenuItem("Exposure");
        MenuItem threat = new MenuItem("Threat");
        MenuItem mitigation = new MenuItem("Mitigation");
        MenuItem counterMitigation = new MenuItem("Counter Mitigation");
        MenuItem action = new MenuItem("Action");
        MenuItem vulnerability = new MenuItem("Vulnerability");
        MenuItem and = new MenuItem("AND");

        // Event handlers for adding nodes
        //topEvent.setOnAction(event -> sc.handleAddNode(NodeType.TOP_EVENT,x, y));
        exposure.setOnAction(event -> nc.handleAddNode(NodeType.EXPOSURE,x, y));
        threat.setOnAction(event -> nc.handleAddNode(NodeType.THREAT,x, y));
        mitigation.setOnAction(event -> nc.handleAddNode(NodeType.MITIGATION,x, y));
        counterMitigation.setOnAction(event -> nc.handleAddNode(NodeType.COUNTER_MITIGATION,x, y));
        action.setOnAction(event -> nc.handleAddNode(NodeType.ACTION,x, y));
        vulnerability.setOnAction(event -> nc.handleAddNode(NodeType.VULNERABILITY,x, y));
        and.setOnAction(event -> nc.handleAddNode(NodeType.AND,x, y));

        nodeMenu.getItems().addAll(
                //topEvent,
                exposure,
                threat,
                mitigation,
                counterMitigation,
                action,
                vulnerability,
                and
        );
        addMenu.getItems().add(nodeMenu);
    }

//    public void initTextBox() {
//        MenuItem textBoxItem = new MenuItem("Text");
//        textBoxItem.setOnAction(e -> {
////            TextArea textArea = new TextArea();
////            textArea.setWrapText(true);
////            // Apply desired styles to the text area
////            textArea.setStyle("-fx-control-inner-background: #ffff99; " +  // Post-it yellow
////                    "-fx-font-family: 'Arial'; " +             // Font style
////                    "-fx-border-color: #cccc33; " +            // Border color
////                    "-fx-border-width: 2; " +                  // Border width
////                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 10);"); // Shadow effect
//
//            // Create a TextBox instance with the styled TextArea
//            TextArea textBox = new TextBox().getTextArea();
//
//            // Access the parent container where you want to add the TextBox
//            Pane root = sc.getRoot();
//            root.getChildren().add(textBox); // Add the TextBox to the root pane
//        });
//        addMenu.getItems().add(textBoxItem);
//    }



}

