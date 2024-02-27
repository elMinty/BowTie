package project.bowtie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project.bowtie.App.Controllers.MenuBarController;

import java.io.IOException;

public class BTApp extends Application {

    public Scene scene;
    public Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
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

    public Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch();
    }
}

