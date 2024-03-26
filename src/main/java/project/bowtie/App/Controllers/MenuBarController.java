package project.bowtie.App.Controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import project.bowtie.App.Controllers.ViewPane.NodeController;
import project.bowtie.IO.*;
import project.bowtie.Model.BTmodel.Nodes.*;

import javax.imageio.ImageIO;
import javax.xml.parsers.*;
import java.io.*;

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

    /**
     * Initializes the menu bar
     * @param scene the scene
     * @param stage the stage
     */
    public void initMenuBar(Scene scene, Stage stage){
        this.scene = scene;
        this.stage = stage;
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
                System.out.println("Top event: " + topEvent.getName());

                // set the top event in the viewPaneController
                if (viewPaneController != null && topEvent != null) {
                    viewPaneController.setTopEvent(topEvent);
                    //print top event id
                    System.out.println("Top event ID: " + topEvent.getId());
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
        // print TopEvent ID
        this.viewPaneController.bowtie.attackTree.getPaths();
    }

    /**
     * Sets the zoom level of the view pane
     * @param zoomFactor the zoom factor
     */
    private void setZoomLevel(double zoomFactor) {
        // Assuming 'viewPaneRoot' is your AnchorPane you want to zoom in/out
        // Set the scroll bars for the scroll Pane
        viewPaneRoot.setPrefSize(3000*zoomFactor, 3000*zoomFactor);
        scrollPane.setHvalue(scrollPane.getHvalue() * zoomFactor);
        scrollPane.setVvalue(scrollPane.getVvalue() * zoomFactor);

        viewPaneController.setScale(zoomFactor); // Set the scale in the ViewPaneController
        // The ScrollPane or any parent container should automatically adjust the scrollbars

        for (javafx.scene.Node child : viewPaneRoot.getChildren()) {
            // Scale each child by the zoomFactor
            child.setScaleX(zoomFactor);
            child.setScaleY(zoomFactor);
        }
    }

    /**
     * Handles zoom actions from the menu items.
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


}
