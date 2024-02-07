package project.bowtie.App.Controllers.ViewPane.Obj;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.TextArea;

public class ResizableTextBox extends StackPane {

    private Rectangle borderRectangle;
    private TextArea textArea;

    public ResizableTextBox(double width, double height) {
        // Initialize the rectangle with provided dimensions
        borderRectangle = new Rectangle(width, height);
        borderRectangle.setStroke(Color.BLACK); // Border color
        borderRectangle.setFill(Color.TRANSPARENT); // Fill color

        // Initialize the text area
        textArea = new TextArea();
        textArea.setPrefSize(width, height);
        textArea.setWrapText(true); // Enable text wrapping

        // Add both the rectangle and text area to the stack pane
        this.getChildren().addAll(borderRectangle, textArea);

        // Implement resizing logic similar to ResizableRectangle
        setupResizeEventHandlers();
    }

    private void setupResizeEventHandlers() {
//        double rectangleStartY;
//        double rectangleStartX;
//        double mouseClickPozY;
//        double mouseClickPozX;
//
//        borderRectangle.setOnMousePressed(event -> {
//            mouseClickPozX = event.getSceneX();
//            mouseClickPozY = event.getSceneY();
//            rectangleStartX = borderRectangle.getWidth();
//            rectangleStartY = borderRectangle.getHeight();
//        });
//
//        borderRectangle.setOnMouseDragged(event -> {
//            double offsetX = event.getSceneX() - mouseClickPozX;
//            double offsetY = event.getSceneY() - mouseClickPozY;
//            double newWidth = rectangleStartX + offsetX;
//            double newHeight = rectangleStartY + offsetY;
//
//            // Ensure minimum size is maintained
//            if (newWidth > MIN_WIDTH) borderRectangle.setWidth(newWidth);
//            if (newHeight > MIN_HEIGHT) borderRectangle.setHeight(newHeight);
//
//            // Resize text area to match the new size of the border
//            textArea.setPrefWidth(borderRectangle.getWidth());
//            textArea.setPrefHeight(borderRectangle.getHeight());
//        });
    }

    // Additional methods to handle resizing, similar to those in ResizableRectangle
}
