package project.bowtie.IO;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Shape;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import project.bowtie.App.Controllers.ViewPane.NodeController;
import project.bowtie.Model.BTmodel.Nodes.Node;
import project.bowtie.Model.BTmodel.Nodes.NodeType;
import project.bowtie.Model.BTmodel.Nodes.NodeUtils;

public class ShapeImporter {



    public static void importShapesFromXML(Document doc, AnchorPane anchorPane, NodeController nodeController) {


        boolean topEventExists = false;
        // Step 1: Parse the XML document and extract shape elements
        NodeList nList = doc.getElementsByTagName("shape");

        //print length of nList
        System.out.println("Length of nList: " + nList.getLength());


        for (int i = 0; i < nList.getLength(); i++) {
            org.w3c.dom.Node domnode = nList.item(i);

            // init params for shape and node
            ////////////////////////////////////////////////////////////////////////////////

            String shapeID = "";
            String shapeType = "";
            double x = 0;
            double y = 0;
            double scaleX = 1;
            double scaleY = 1;


            String nodeID   = "";
            String nodeName = "";
            String nodeType = "";
            String nodeDescription = "";
            String nodeScore = "";



            ////////////////////////////////////////////////////////////////////////////////


            if (domnode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element element = (Element) domnode;

                // Extract the type of shape
                shapeType = element.getAttribute("type");
                shapeID = element.getAttribute("id");

                // get the x and y coordinates
                x = Double.parseDouble(element.getAttribute("x"));
                y = Double.parseDouble(element.getAttribute("y"));

                // get the scale of the shape
                scaleX = Double.parseDouble(element.getAttribute("scaleX"));
                scaleY = Double.parseDouble(element.getAttribute("scaleY"));

                // get node from xml
                // Assuming there is only one Node element per Shape
                NodeList nodeList = element.getElementsByTagName("node");
                if (nodeList.getLength() > 0) {
                    org.w3c.dom.Node node = nodeList.item(0);
                    if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                        Element nodeElement = (Element) node;
                        // Extract information from the Node element, such as an ID or other attributes
                        nodeID = nodeElement.getAttribute("id");
                        nodeName = nodeElement.getAttribute("name");
                        nodeType = nodeElement.getAttribute("type");
                        nodeDescription = nodeElement.getAttribute("description");
                        nodeScore = nodeElement.getAttribute("score");


                    }
                }

                NodeType type = NodeUtils.getNodeType(nodeType);

                if (type == null) {
                    System.out.println("Unsupported node type: " + nodeType);
                    continue;
                }
                else if (type == NodeType.TOP_EVENT){
                    if (topEventExists) {
                        System.out.println("Top event already exists");
                        break;
                    }
                    Shape shape = nodeController.LoadNode(NodeType.TOP_EVENT, x, y, nodeID, nodeName, nodeDescription, nodeScore, scaleX, scaleY);
                    topEventExists = true;
                }
                else {
                    Shape shape = nodeController.LoadNode(type, x, y, nodeID, nodeName, nodeDescription, nodeScore, scaleX, scaleY);
                }
            }
        }
    }
}
