package BowTie;

import BowTie.Controllers.BTController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * JavaFX App
 *
 * This class is the entry point for the application. It loads the main.fxml file and sets up the stage.
 * Sets the controller as BTController and initializes it.
 *
 */
public class BTApp extends Application {

    public Scene scene; // Main scene
    public Stage stage; // Main stage

    /**
     * Start method for the application
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage; // Set the stage
        FXMLLoader fxmlLoader = new FXMLLoader(BTApp.class.getResource("main.fxml")); // Load the main.fxml file
        scene = new Scene(fxmlLoader.load(), 3000, 3000); // Set the scene


        BTController controller = fxmlLoader.getController(); // Get the controller
        controller.initController(scene, stage); // Initialize the controller


        // set actual size as max
        stage.setMaxHeight(3000);
        stage.setMaxWidth(3000);

        // Set the stage
        stage.setTitle("BowTie");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    /**
     * Main method
     * @param args Not necessary
     */
    public static void main(String[] args) {
        launch();
    }
}

