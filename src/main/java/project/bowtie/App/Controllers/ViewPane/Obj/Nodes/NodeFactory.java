package project.bowtie.App.Controllers.ViewPane.Obj.Nodes;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import project.bowtie.Model.BTmodel.Nodes.NodeType;

/**
 * A factory class that creates different types of shapes for the view pane based on the NodeType.
 */
public class NodeFactory{

    public static Shape createNode(NodeType type) {

        Shape shape = null;
        String label = "";
        if (type != null) {
            switch (type) {
                case TOP_EVENT:
                    shape = createThreatNode(NodeType.TOP_EVENT);
                    break;

                case THREAT:
                    shape = createThreatNode(NodeType.THREAT);
                    break;

                case MITIGATION:
                    // Octogon
                    shape = new Polygon(new double[]{
                            45.730,0.000,
                            32.336,32.336,
                            0.000,45.730,
                            -32.336,32.336,
                            -45.730,0.000,
                            -32.336,-32.336,
                            0.000,-45.730,
                            32.336,-32.336});
                    shape.setFill(Color.ORANGE);
                    label = "Mitigation";
                    break;
                case COUNTER_MITIGATION:
                    // Pentagon
                    shape = new Polygon(new double[]{42.533,0.000,13.143,40.451,-34.410,25.000,-34.410,-25.000,13.143,-40.451});
                    shape.setFill(Color.GREEN);
                    shape.setRotate(55);
                    label = "Counter Mitigation";
                    break;
                case EXPOSURE:
                    // circle
                    shape = new Circle(35);
                    shape.setFill(Color.LIGHTSKYBLUE);
                    label = "Exposure";
                    break;
                case ACTION:
                    // square
                    shape = new Rectangle(100, 100);
                    shape.setFill(Color.WHITE);
                    label = "Action";
                    break;
                case VULNERABILITY:
                    // diamond
                    shape = new Polygon(new double[]{0,86.603,50.0,0,0,-86.603,-50.0,0});
                    shape.setRotate(45);

                    shape.setFill(Color.BLACK);
                    label = "Vulnerability";
                    break;

                case AND:
                    // LOGICAL AND - BLACK OUTLINED small circle
                    shape = new Circle(10);
                    shape.setFill(Color.WHITE);
                    shape.setStroke(Color.BLACK);
                    shape.setStrokeWidth(2);
                    label = "AND";
                    break;
            }
        }


        return shape;

    }

    public static Shape createThreatNode(NodeType type) {
        // Create a circle
        Circle circle = new Circle(50, 100, 15);


        // Create a triangle
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(new Double[]{
                50.0, 100.0,  // Point at the bottom of the circle
                0.0, 200.0,   // Left corner of the triangle
                100.0, 200.0  // Right corner of the triangle
        });


        // Combine the circle and triangle into a single Shape
        Shape combinedShape = Shape.union(circle, triangle);
        combinedShape.setRotate(-90);     // Rotate the shape

        if (type == NodeType.TOP_EVENT) {
            combinedShape.setFill(Color.BLACK);
        } else if (type == NodeType.THREAT) {
            combinedShape.setFill(Color.RED);
        }

        // Label is not a property of Shape, so you would need to handle it separately
        String label = "Threat";

        return combinedShape;
    }

}
