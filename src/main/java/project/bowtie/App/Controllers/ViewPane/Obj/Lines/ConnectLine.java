package project.bowtie.App.Controllers.ViewPane.Obj.Lines;

import javafx.beans.binding.Bindings;
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
        // Calculate and bind start point to the source shape's center
        Point2D sourceCenter = getShapeCenter(sourceShape, connectType, true);
        startXProperty().bind(Bindings.add(sourceShape.layoutXProperty(), sourceShape.translateXProperty()).add(sourceCenter.getX()));
        startYProperty().bind(Bindings.add(sourceShape.layoutYProperty(), sourceShape.translateYProperty()).add(sourceCenter.getY()));

        // Calculate and bind end point to the target shape's center
        Point2D targetCenter = getShapeCenter(targetShape, connectType, false);
        endXProperty().bind(Bindings.add(targetShape.layoutXProperty(), targetShape.translateXProperty()).add(targetCenter.getX()));
        endYProperty().bind(Bindings.add(targetShape.layoutYProperty(), targetShape.translateYProperty()).add(targetCenter.getY()));

        // Add this line to the parent container
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