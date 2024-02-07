package project.bowtie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BTApp extends Application {

    public Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BTApp.class.getResource("main.fxml"));
        scene = new Scene(fxmlLoader.load(), 1200, 720);


        BTController controller = fxmlLoader.getController();
        controller.initController(scene, stage);

        stage.setTitle("BowTie");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
