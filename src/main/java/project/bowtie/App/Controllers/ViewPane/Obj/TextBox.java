package project.bowtie.App.Controllers.ViewPane.Obj;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

// ResizableTextArea class
public class TextBox extends StackPane {
    private static final double RESIZE_HANDLE_SIZE = 15;

    public TextBox(TextArea textArea) {
        // Set text area to be fill the stack pane
        StackPane.setAlignment(textArea, Pos.TOP_LEFT);
        this.getChildren().add(textArea);

        // Create a resize handle
        Rectangle resizeHandle = new Rectangle(RESIZE_HANDLE_SIZE, RESIZE_HANDLE_SIZE);
        resizeHandle.setFill(javafx.scene.paint.Color.GRAY); // Set color of the resize handle
        resizeHandle.setCursor(Cursor.SE_RESIZE); // Set cursor when mouse over handle

        // Bind the position of the resize handle to the bottom right of the text area
        resizeHandle.layoutXProperty().bind(textArea.widthProperty().subtract(RESIZE_HANDLE_SIZE));
        resizeHandle.layoutYProperty().bind(textArea.heightProperty().subtract(RESIZE_HANDLE_SIZE));

        // Handle the resize logic
        resizeHandle.setOnMouseDragged(event -> {
            double newWidth = event.getX();
            double newHeight = event.getY();
            textArea.setPrefSize(Math.max(textArea.getMinWidth(), newWidth),
                    Math.max(textArea.getMinHeight(), newHeight));
        });

        this.getChildren().add(resizeHandle);
    }
}
