package project.bowtie.App.Controllers.ViewPane.Obj.Lines;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.*;
import project.bowtie.App.Controllers.ViewPane.Menus.ConnectionMode;

/**
 * A line that connects two shapes in the view pane.
 * @see project.bowtie.App.Controllers.ViewPane.Menus.ConnectionMode
 * @see project.bowtie.App.Controllers.ViewPane.Obj.Lines.Connector
 */
public class ConnectLine extends Line {

    public ConnectLine(Shape sourceShape, Shape targetShape, ConnectionMode connectType) {
        AnchorPane parent = (AnchorPane) sourceShape.getParent();

        // Dynamically bind start and end points to the source and target shapes' center points, considering scale
        DoubleBinding startXBinding = Bindings.createDoubleBinding(() ->
                        sourceShape.getLayoutX() + sourceShape.getTranslateX() + getShapeCenter(sourceShape, connectType, true).getX() * sourceShape.getScaleX(),
                sourceShape.layoutXProperty(), sourceShape.translateXProperty(), sourceShape.scaleXProperty());

        DoubleBinding startYBinding = Bindings.createDoubleBinding(() ->
                        sourceShape.getLayoutY() + sourceShape.getTranslateY() + getShapeCenter(sourceShape, connectType, true).getY() * sourceShape.getScaleY(),
                sourceShape.layoutYProperty(), sourceShape.translateYProperty(), sourceShape.scaleYProperty());

        DoubleBinding endXBinding = Bindings.createDoubleBinding(() ->
                        targetShape.getLayoutX() + targetShape.getTranslateX() + getShapeCenter(targetShape, connectType, false).getX() * targetShape.getScaleX(),
                targetShape.layoutXProperty(), targetShape.translateXProperty(), targetShape.scaleXProperty());

        DoubleBinding endYBinding = Bindings.createDoubleBinding(() ->
                        targetShape.getLayoutY() + targetShape.getTranslateY() + getShapeCenter(targetShape, connectType, false).getY() * targetShape.getScaleY(),
                targetShape.layoutYProperty(), targetShape.translateYProperty(), targetShape.scaleYProperty());

        // Apply the bindings
        startXProperty().bind(startXBinding);
        startYProperty().bind(startYBinding);
        endXProperty().bind(endXBinding);
        endYProperty().bind(endYBinding);

        // Add this line to the parent container if not already present
        if (!parent.getChildren().contains(this)) {
            parent.getChildren().add(this);
        }
    }

    /**
     * Get the center of a shape based on the connection mode and whether it is the source or target shape.
     * @param shape The shape to get the center of
     * @param conn The connection mode
     * @param isSource Whether the shape is the source or target shape
     * @return The center of the shape
     */
    public static Point2D getShapeCenter(Shape shape, ConnectionMode conn, boolean isSource) {
        double x;
        double y;

        if (shape instanceof Circle) {
            Circle circle = (Circle) shape;
            x = circle.getCenterX();
            y = circle.getCenterY();

        } else if (shape instanceof Rectangle) {
            Rectangle rect = (Rectangle) shape;
            x = rect.getX() + rect.getWidth() / 2;
            y = rect.getY() + rect.getHeight() / 2;

        } else if (shape instanceof Polygon) {
            Polygon polygon = (Polygon) shape;
            double avgX = 0, avgY = 0;
            for (int i = 0; i < polygon.getPoints().size(); i += 2) {
                avgX += polygon.getPoints().get(i);
                avgY += polygon.getPoints().get(i + 1);
            }
            x = avgX / (polygon.getPoints().size() / 2);
            y = avgY / (polygon.getPoints().size() / 2);

        } else {
            // General approach for any shape
            Bounds bounds = shape.getBoundsInLocal();
            x = bounds.getMinX() + bounds.getWidth() / 2;
            y = bounds.getMinY() + bounds.getHeight() / 2;

        }

        switch (conn) {
            case BEFORE:
                if (isSource) {
                    x -= shape.getBoundsInParent().getWidth() / 2; // Move X to the right
                } else {
                    x += shape.getBoundsInParent().getWidth() / 2; // Move X to the left
                }
                break;
            case AFTER:
                if (isSource) {
                    x += shape.getBoundsInParent().getWidth() / 2; // Move X to the left
                } else {
                    x -= shape.getBoundsInParent().getWidth() / 2; // Move X to the right
                }
                break;
            case MITIGATE:
                 if (isSource) { // connect to top else connect to bottom
                     y -= shape.getBoundsInParent().getHeight() / 2; // Move Y to the bottom
                    } else {
                        y += shape.getBoundsInParent().getHeight() / 2; // Move Y to the top
                    }
                break;
        }
        return new Point2D(x, y);
    }
}