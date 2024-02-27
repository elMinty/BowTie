package project.bowtie.App.Controllers;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.BufferedWriter;

import javafx.embed.swing.SwingFXUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import project.bowtie.App.Controllers.ViewPane.NodeController;
import project.bowtie.IO.ShapeExporter;
import project.bowtie.IO.ShapeImporter;

import java.io.FileWriter;


import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static project.bowtie.IO.ShapeImporter.importShapesFromXML;

public class MenuBarController{

    private Stage mainStage;
    private Scene scene;
    private Stage stage;
    private AnchorPane viewPaneRoot;
    private NodeController nodeController;


    public void initMenuBar(Scene scene, Stage stage){
        this.scene = scene;
        this.stage = stage;
    }


    public void setViewPaneRoot(AnchorPane root) {
        this.viewPaneRoot = root;
    }

    public void setNodeController(NodeController nodeController) {
        this.nodeController = nodeController;
    }


    public void handleSaveAction() {
        FileChooser fileChooser = new FileChooser();
        // Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        // set initial directory for current directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        // Show save file dialog
        Stage stage = (Stage) viewPaneRoot.getScene().getWindow(); // Assuming 'anchorPane' is your AnchorPane instance
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            String xmlContent = ShapeExporter.exportShapesToXML(viewPaneRoot); // Call your export function
            saveTextToFile(xmlContent, file);
        }
    }

    private void saveTextToFile(String content, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., show an error dialog)
        }
    }

    public void handleClose(ActionEvent actionEvent) {
        System.out.println("Close action triggered");
        stage.close();
    }

    public void handleScreenshot(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        // Show save file dialog
        File file = fileChooser.showSaveDialog(stage); // 'stage' should be your current window's stage

        if (file != null) {
            try {
                // Take screenshot of the scene or specific node
                WritableImage writableImage = new WritableImage((int)scene.getWidth(), (int)scene.getHeight());
                scene.snapshot(writableImage); // 'scene' should be the scene you want to capture

                // Save the screenshot
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            } catch (IOException ex) {
                // Handle exception
            }
        }
    }


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

                // import the shapes from the XML file
                importShapesFromXML(doc, viewPaneRoot, nodeController); // Call your export function
            } catch (SAXException | ParserConfigurationException | IOException e) {
                throw new RuntimeException(e);
            }


        }
    }
}
