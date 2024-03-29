package BowTie.Controllers;

import BowTie.Controllers.ViewPane.NodeController;
import BowTie.IO.ShapeExporter;
import BowTie.IO.ShapeImporter;
import BowTie.Model.Nodes.Node;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.util.List;

/**
 * Controller for the menu bar - handles save, open, screenshot, and close actions
 */
public class MenuBarController{

    private Stage mainStage;
    private Scene scene;
    private Stage stage;
    private AnchorPane viewPaneRoot;
    @FXML
    private javafx.scene.control.ScrollPane scrollPane;
    private NodeController nodeController;
    private ViewPaneController viewPaneController;
    private boolean closeFlag = false;
    private PathPaneController pathPaneController;

    /**
     * Initializes the menu bar
     * @param scene the scene
     * @param stage the stage
     */
    public void initMenuBar(Scene scene, Stage stage, PathPaneController pathPaneController){
        this.scene = scene;
        this.stage = stage;
        this.pathPaneController = pathPaneController;
    }

    /**
     * Sets the root of the view pane
     * @param root the root of the view pane
     */
    public void setViewPaneRoot(AnchorPane root, ScrollPane scrollPane) {
        this.viewPaneRoot = root;
        this.scrollPane = scrollPane;
    }

    /**
     * Sets the node controller
     * @param nodeController the node controller
     */
    public void setNodeController(NodeController nodeController) {
        this.nodeController = nodeController;
    }

    /**
     * Handles the save action
     */
    public void handleSaveAction() {
        FileChooser fileChooser = new FileChooser();
        // Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        // set initial directory for current directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        //Change dialog if Save on close
        if (closeFlag) {
            // Show dialog - Do you want to save before closing?
            fileChooser.setTitle("Unsaved Changes... Save before closing?");
            fileChooser.setInitialFileName("Bowtie");
        }

        // Show save file dialog
        Stage stage = (Stage) viewPaneRoot.getScene().getWindow(); // Assuming 'anchorPane' is your AnchorPane instance
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            String xmlContent = ShapeExporter.exportShapesToXML(viewPaneRoot); // Call your export function
            saveTextToFile(xmlContent, file);
        }
    }

    /**
     * Saves the content to a file
     * @param content the content to save
     * @param file the file to save to
     */
    private void saveTextToFile(String content, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., show an error dialog)
        }
    }

    /**
     * Handles the close action
     * @param actionEvent the action event
     */
    public void handleClose(ActionEvent actionEvent) {
        // Check if user wants to Save
        closeFlag = true;
        handleSaveAction();
        System.out.println("Close action triggered");
        stage.close();
    }

    /**
     * Handles the screenshot action - Screenshots to PNG
     * @param actionEvent the action event
     */
    public void handleScreenshot(ActionEvent actionEvent) { // Include AnchorPane in the arguments

        FileChooser fileChooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        // Show save file dialog
        File file = fileChooser.showSaveDialog(stage); // 'stage' should be your current window's stage

        if (file != null) {
            try {
                // Take screenshot of the AnchorPane
                WritableImage writableImage = new WritableImage((int)viewPaneRoot.getWidth(), (int)viewPaneRoot.getHeight());
                viewPaneRoot.snapshot(new SnapshotParameters(), writableImage); // Use the anchorPane for snapshot

                // Save the screenshot
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            } catch (IOException ex) {
                // Handle exception
            }
        }
    }

    /**
     * Handles the open action
     * @param actionEvent the action event
     */
    public void handleOpenAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        // Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        // set initial directory for current directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        // Show save file dialog
        Stage stage = (Stage) viewPaneRoot.getScene().getWindow(); // Assuming 'anchorPane' is your AnchorPane instance
        File xmlFile = fileChooser.showOpenDialog(stage);

        // Check if file is not null and has an XML extension
        if (xmlFile != null && xmlFile.getName().endsWith(".xml")){

            // parser for the XML file
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;

            Document doc = null;
            try {
                dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(xmlFile);
                doc.getDocumentElement().normalize();

                // clear the viewPaneRoot
                viewPaneRoot.getChildren().clear();

                ShapeImporter shapeImporter = new ShapeImporter();
                // import the shapes from the XML file
                Node topEvent = shapeImporter.importShapesFromXML(doc, viewPaneRoot, nodeController); // Call your export function

                // set the top event in the viewPaneController
                if (viewPaneController != null && topEvent != null) {
                    viewPaneController.setTopEvent(topEvent);
                }
            } catch (SAXException | ParserConfigurationException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Sets the view pane controller
     * @param viewPaneController the view pane controller
     */
    public void setVPC(ViewPaneController viewPaneController) {
        this.viewPaneController = viewPaneController;
    }

    /**
     * Handles the path action
     * @param actionEvent the action event
     */
    public void handlePath(ActionEvent actionEvent) {
        List<String> consequences = this.viewPaneController.bowtie.consequenceTree.generateAllPaths();
        List<String> attacks = this.viewPaneController.bowtie.attackTree.generateAllPaths();
        List<List<Integer>> attackPathIDs = this.viewPaneController.bowtie.attackTree.generatePathIDs();
        List<List<Integer>> consequencePathIDs = this.viewPaneController.bowtie.consequenceTree.generatePathIDs();
        pathPaneController.updatePathMaps(attacks, consequences, attackPathIDs, consequencePathIDs);
    }


    /**
     * Handles the color action and changes the background colour
     * @param e the action event
     */
    @FXML
    private void handleColor(ActionEvent e) {
        // get color from colour wheel
        ColorPicker picker = new ColorPicker();
        viewPaneRoot.getChildren().add(picker);
        // Color change listener
        picker.setOnAction(event -> {
            // Remove the picker from its parent
            viewPaneController.setColor(picker.getValue());
            viewPaneRoot.getChildren().remove(picker);
        });
    }

    public void handleValid(ActionEvent actionEvent) {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    // @deprecated \\ - Needs some fixes
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Sets the zoom level of the view pane
     * @param zoomFactor the zoom factor
     * @deprecated
     */
    private void setZoomLevel(double zoomFactor) {
        // Assuming 'viewPaneRoot' is your AnchorPane you want to zoom in/out
        // Set the scroll bars for the scroll Pane
        viewPaneRoot.setPrefSize(3000*zoomFactor, 3000*zoomFactor);
        scrollPane.setHvalue(scrollPane.getHvalue() * zoomFactor);
        scrollPane.setVvalue(scrollPane.getVvalue() * zoomFactor);

        viewPaneController.setScale(zoomFactor); // Set the scale in the ViewPaneController
        // The ScrollPane or any parent container should automatically adjust the scrollbars

        // Apply zoom and translation to the children of the view pane
        applyZoomAndTranslation(zoomFactor);
    }

    /**
     * Handles zoom actions from the menu items.
     * @param event the action event
     * @deprecated
     */
    @FXML
    private void handleZoomAction(ActionEvent event) {
        if (event.getSource() instanceof MenuItem) {
            MenuItem menuItem = (MenuItem) event.getSource();
            String text = menuItem.getText();
            // Extract the numeric part from the menu item text (e.g., "100%" -> 100)
            double zoomPercentage = Double.parseDouble(text.replace("%", ""));
            setZoomLevel(zoomPercentage / 100); // Convert to scale factor (e.g., 1.0 for 100%)
        }
    }

    /**
     *
     * @param zoomFactor 0.5, 0.75, 1.0, 1.25, 1.5
     * @deprecated
     */
    private void applyZoomAndTranslation(double zoomFactor) {

        Point2D fixedPoint = new Point2D(0,0); // Fixed point (center of the view pane

        for (javafx.scene.Node child : viewPaneRoot.getChildren()) {
            // Calculate the original center of the shape
            Point2D originalCenter = new Point2D(child.getLayoutBounds().getWidth() / 2, child.getLayoutBounds().getHeight() / 2);
            Point2D originalCenterInParent = child.localToParent(originalCenter);

            // Apply zoom
            child.setScaleX(zoomFactor);
            child.setScaleY(zoomFactor);

            // Calculate the new center of the shape after scaling
            Point2D newCenter = new Point2D(child.getLayoutBounds().getWidth() / 2 * zoomFactor, child.getLayoutBounds().getHeight() / 2 * zoomFactor);
            Point2D newCenterInParent = child.localToParent(newCenter);

            // Calculate the delta from the fixed point
            Point2D deltaFromFixedPoint = originalCenterInParent.subtract(fixedPoint);

            // Adjust the position to keep the shape's center correctly positioned relative to the zoom
            Point2D newPosition = fixedPoint.add(deltaFromFixedPoint.multiply(zoomFactor)).subtract(newCenter);
            child.setLayoutX(newPosition.getX());
            child.setLayoutY(newPosition.getY());
        }
    }


    @FXML
    private void handleOpenLinkAction() {
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI("https://github.com/elMinty/BowTie"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
