package BowTie.IO;

import BowTie.Model.Nodes.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Shape;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
import java.util.Map;

/**
 * Exports the bowtie model - shapes and nodes - to an XML file
 */
public class ShapeExporter {

    /**
     * Exports the shapes and nodes to an XML file
     * @param anchorPane the anchor pane
     * @return the XML file as a string
     */
    public static String exportShapesToXML(AnchorPane anchorPane) {
        List<Shape> shapes = new ArrayList<>();
        // Iterate through children and find shapes
        for (var child : anchorPane.getChildren()) {
            if (child instanceof Shape) {
                shapes.add((Shape) child);
            }
        }

        try {
            // Create XML document
            ////////////////////////////////////////////////////////////////////////
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("shapes");
            doc.appendChild(rootElement);

            // Convert shapes to XML elements
            ////////////////////////////////////////////////////////////////////////
            for (Shape shape : shapes) {
                if (shape.getUserData() == null) {
                    System.out.println("Shape has no user data");
                    continue;
                }
                Element shapeElement = setElements(doc,shape);
                // Add more properties as needed
                rootElement.appendChild(shapeElement);
            }

            // Convert to string for output
            ////////////////////////////////////////////////////////////////////////
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

    /**
     * Sets the xml elements for the shape displayed on the Pane
     * @param doc the document
     * @param shape the shape
     * @return the element
     */
    private static Element setElements(Document doc,Shape shape) {

        System.out.println("Setting elements");
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

        // Node elements for the shape - append to shape element
        Element nodeElement = setNodeElements(doc,node);
        shapeElement.appendChild(nodeElement);

        return shapeElement;
    }

    /**
     * Sets the xml elements for the node - from the bowtie model
     * @param doc the document
     * @param node the node
     * @return the element
     */
    private static Element setNodeElements(Document doc,Node node) {
        // Add node attribute elements
        Element nodeElement = doc.createElement("node");
        nodeElement.setAttribute("id", node.getId());
        nodeElement.setAttribute("type", node.getType().toString());
        nodeElement.setAttribute("name", node.getName());
        nodeElement.setAttribute("description", node.getDescription());
        nodeElement.setAttribute("score", node.getScore());

        // add appending nodes
        Element beforeNodes = doc.createElement("beforeNodes");
        Element afterNodes = doc.createElement("afterNodes");
        Element mitigationNodes = doc.createElement("mitigationNodes");
        beforeNodes.setAttribute("size", String.valueOf(node.getBeforeNodes().size()));
        afterNodes.setAttribute("size", String.valueOf(node.getAfterNodes().size()));
        mitigationNodes.setAttribute("size", String.valueOf(node.getMitigationNodes().size()));
        String beforeKeys = getKeys(node.getBeforeNodes());
        String afterKeys = getKeys(node.getAfterNodes());
        String mitigationKeys = getKeys(node.getMitigationNodes());

        beforeNodes.setAttribute("keys", beforeKeys);
        afterNodes.setAttribute("keys", afterKeys);
        mitigationNodes.setAttribute("keys", mitigationKeys);

        nodeElement.appendChild(beforeNodes);
        nodeElement.appendChild(afterNodes);
        nodeElement.appendChild(mitigationNodes);

        return nodeElement;
    }

    /**
     * Gets the keys of the map -> converts to a string
     * @param map the map
     * @return the keys as a string separated by commas
     */
    private static String getKeys(Map<String, Node> map) {
        String keys = "";
        if (map.isEmpty()) {
            return keys;
        }
        int index = 0;
        for (String key : map.keySet()) {

            keys += key;
            //if not last key, add comma
            if (index < map.size() - 1) {
                keys += ",";
            }
            index++;
        }
        return keys;
    }
}
