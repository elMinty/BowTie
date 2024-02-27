package project.bowtie.IO;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Shape;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import project.bowtie.App.Controllers.ViewPane.NodeController;
import project.bowtie.App.Controllers.ViewPane.Obj.Lines.Connector;
import project.bowtie.Model.BTmodel.Nodes.Node;
import project.bowtie.Model.BTmodel.Nodes.NodeType;
import project.bowtie.Model.BTmodel.Nodes.NodeUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShapeImporter {

    NodeController nodeController;
    Connector nodeConnector;

    public Node importShapesFromXML(Document doc, AnchorPane anchorPane, NodeController nodeController) {
        this.nodeController = nodeController;
        this.nodeConnector = nodeController.connector;

        Node topEvent = null;

        boolean topEventExists = false;
        // Step 1: Parse the XML document and extract shape elements
        NodeList nList = doc.getElementsByTagName("shape");
        Map<String, Shape> shapeMap = new HashMap<>();

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
                // only 1 top event allowed
                else if (type == NodeType.TOP_EVENT){
                    if (topEventExists) {
                        System.out.println("Top event already exists");
                        break;
                    }
                    Shape shape = nodeController.LoadNode(NodeType.TOP_EVENT, x, y, nodeID, nodeName, nodeDescription, nodeScore, scaleX, scaleY);
                    topEventExists = true;
                    topEvent = (Node) shape.getUserData();
                    shapeMap.put(nodeID, shape);
                }
                else {
                    Shape shape = nodeController.LoadNode(type, x, y, nodeID, nodeName, nodeDescription, nodeScore, scaleX, scaleY);
                    shapeMap.put(nodeID, shape);
                }



            }
        }

        // Connect the shapes
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Map<String, NodeConnections> nodeConnectionsMap = parseNodeConnections(doc);
        for (Map.Entry<String, NodeConnections> entry : nodeConnectionsMap.entrySet()) {
            String nodeId = entry.getKey();
            NodeConnections connections = entry.getValue();
            for (String beforeNodeId : connections.getBeforeNodes()) {
                nodeConnector.load_connection(nodeId, beforeNodeId, shapeMap.get(nodeId), shapeMap.get(beforeNodeId));
            }
        }


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

        return topEvent;

    }


    // Parse the connections between nodes
    public static Map<String, NodeConnections> parseNodeConnections(Document doc) {
        Map<String, NodeConnections> nodeConnectionsMap = new HashMap<>();

        NodeList shapeList = doc.getElementsByTagName("shape");
        for (int i = 0; i < shapeList.getLength(); i++) {
            Element shape = (Element) shapeList.item(i);
            Element node = (Element) shape.getElementsByTagName("node").item(0);
            String nodeId = node.getAttribute("id");

            // parse before nodes
            Element beforeNodesElement = (Element) node.getElementsByTagName("beforeNodes").item(0);
            String beforeKeys = beforeNodesElement.getAttribute("keys");
            List<String> beforeList = Arrays.asList(beforeKeys.split(",")).stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());

            // parse after nodes
            Element afterNodesElement = (Element) node.getElementsByTagName("afterNodes").item(0);
            String afterKeys = afterNodesElement.getAttribute("keys");
            List<String> afterList = Arrays.asList(afterKeys.split(",")).stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());

            nodeConnectionsMap.put(nodeId, new NodeConnections(beforeList , afterList));
        }

        return nodeConnectionsMap;
    }

}

class NodeConnections {
    public List<String> beforeNodes;
    public List<String> afterNodes;

    public NodeConnections(List<String> beforeNodes , List<String> afterNodes) {
        this.beforeNodes = beforeNodes;
        this.afterNodes = afterNodes;
    }

    // Getters and setters for beforeNodes and afterNodes

    public List<String> getBeforeNodes() {
        return beforeNodes;
    }

    public void setBeforeNodes(List<String> beforeNodes) {
        this.beforeNodes = beforeNodes;
    }

    public List<String> getAfterNodes() {
        return afterNodes;
    }

    public void setAfterNodes(List<String> afterNodes) {
        this.afterNodes = afterNodes;
    }
}