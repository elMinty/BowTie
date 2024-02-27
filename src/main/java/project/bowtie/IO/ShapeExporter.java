package project.bowtie.IO;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Shape;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import project.bowtie.Model.BTmodel.Nodes.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class ShapeExporter {

    public static String exportShapesToXML(AnchorPane anchorPane) {
        List<Shape> shapes = new ArrayList<>();
        // Step 1: Iterate through children and find shapes
        for (var child : anchorPane.getChildren()) {
            if (child instanceof Shape) {
                shapes.add((Shape) child);
            }
        }

        try {
            // Create XML document
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("shapes");
            doc.appendChild(rootElement);

            // Step 2 & 3: Convert shapes to XML elements
            for (Shape shape : shapes) {

                Element shapeElement = setElements(doc,shape);
                // Add more properties as needed
                rootElement.appendChild(shapeElement);
            }

            // Convert to string for output
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Element setElements(Document doc,Shape shape) {
        // Node elements for the shape
        Node node = (Node) shape.getUserData();

        Element shapeElement = doc.createElement("shape");
        shapeElement.setAttribute("id", node.getId()); // Shape ID
        shapeElement.setAttribute("type", node.getType().toString()); // Node type

        // Location Elements
        shapeElement.setAttribute("x", String.valueOf(shape.getLayoutX())); // X position
        shapeElement.setAttribute("y", String.valueOf(shape.getLayoutY())); // Y position

        // Scale Elements
        shapeElement.setAttribute("scaleX", String.valueOf(shape.getScaleX())); // X scale
        shapeElement.setAttribute("scaleY", String.valueOf(shape.getScaleY())); // Y scale

        // Node elements for the shape
        Element nodeElement = setNodeElements(doc,node);

        shapeElement.appendChild(nodeElement);


        return shapeElement;
    }

    private static Element setNodeElements(Document doc,Node node) {
        // Node elements for the shape
        Element nodeElement = doc.createElement("node");
        nodeElement.setAttribute("id", node.getId());
        nodeElement.setAttribute("type", node.getType().toString());
        nodeElement.setAttribute("name", node.getName());
        nodeElement.setAttribute("description", node.getDescription());
        nodeElement.setAttribute("score", node.getScore());

        return nodeElement;
    }
}
