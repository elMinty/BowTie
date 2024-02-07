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

        // Event handlers for adding nodes
        //topEvent.setOnAction(event -> sc.handleAddNode(NodeType.TOP_EVENT,x, y));
        exposure.setOnAction(event -> sc.handleAddNode(NodeType.EXPOSURE,x, y));
        threat.setOnAction(event -> sc.handleAddNode(NodeType.THREAT,x, y));
        mitigation.setOnAction(event -> sc.handleAddNode(NodeType.MITIGATION,x, y));
        counterMitigation.setOnAction(event -> sc.handleAddNode(NodeType.COUNTER_MITIGATION,x, y));
        action.setOnAction(event -> sc.handleAddNode(NodeType.ACTION,x, y));
        vulnerability.setOnAction(event -> sc.handleAddNode(NodeType.VULNERABILITY,x, y));

        nodeMenu.getItems().addAll(
                //topEvent,
                exposure,
                threat,
                mitigation,
                counterMitigation,
                action,
                vulnerability
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

