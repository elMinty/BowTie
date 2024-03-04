package project.bowtie.App.Controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import project.bowtie.App.Controllers.ViewPane.NodeController;
import project.bowtie.IO.ShapeExporter;
import project.bowtie.IO.ShapeImporter;
import project.bowtie.Model.BTmodel.Bowtie.AttackTree;
import project.bowtie.Model.BTmodel.Nodes.Node;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MenuBarController{

    private Stage mainStage;
    private Scene scene;
    private Stage stage;
    private AnchorPane viewPaneRoot;
    private NodeController nodeController;
    private ViewPaneController viewPaneController;


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

    public void setVPC(ViewPaneController viewPaneController) {
        this.viewPaneController = viewPaneController;
    }

    public void handlePath(ActionEvent actionEvent) {
        // print TopEvent ID
        this.viewPaneController.bowtie.attackTree.getPaths();

    }
}
