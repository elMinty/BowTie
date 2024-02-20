package project.bowtie.App.Controllers.ViewPane.Obj;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Shape;
import project.bowtie.App.Controllers.ViewPane.Menus.ConnectionMode;
import project.bowtie.Model.BTmodel.Nodes.Node;
import project.bowtie.Model.BTmodel.Nodes.NodeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Connector {
    private Map<String, Node> nodeMap;
    // In your connection management logic:
    Map<String, ConnectLine> connections = new HashMap<>();

    private String sourceNodeId;
    private String targetNodeId;

    private Shape sourceShape;
    private Shape targetShape;

    private boolean connectMode = false;
    private ConnectionMode connectType = ConnectionMode.NONE;
    private boolean viableConnection = false;

    public Connector(Map<String, Node> nodeMap) {
        resetConnector();
        this.nodeMap = nodeMap;
    }

    // flush the connector -- this is called when the user clicks on a shape and connection mode is active
    public void flush(Shape targetShape) {
        this.targetShape = targetShape;
        try{
            if (isViableConnection(targetNodeId, sourceNodeId) && connectMode) {
                connect(connectType);
            }
            else {
                connectAlert(ConnectionMode.NONE);
            }
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception");
        }


        resetConnector();

    }

    // connection type
    private void connect(ConnectionMode connectType) {
        switch (connectType) {
            case BEFORE:
                if (!NodeUtils.connect(nodeMap.get(targetNodeId), nodeMap.get(sourceNodeId))){
                    connectAlert(ConnectionMode.BEFORE);}
                else {

                Node sourceNode = nodeMap.get(sourceNodeId);
                sourceNode.listAdjoiningNodes();

                Node targetNode = nodeMap.get(targetNodeId);
                targetNode.listAdjoiningNodes();
                // Instantiate ConnectLine and bind it to the shapes
                ConnectLine line = new ConnectLine(sourceShape, targetShape, ConnectionMode.BEFORE);

                //Store the connection
                String hash = generateConnectionHash(sourceNodeId, targetNodeId);
                connections.put(hash, line);

                }

                break;
            case AFTER:
                if (!NodeUtils.connect(nodeMap.get(sourceNodeId), nodeMap.get(targetNodeId))){connectAlert(ConnectionMode.AFTER);}
                else {

                Node sourceNode = nodeMap.get(sourceNodeId);
                sourceNode.listAdjoiningNodes();
                // Instantiate ConnectLine and bind it to the shapes
                ConnectLine line = new ConnectLine(sourceShape, targetShape, ConnectionMode.AFTER);
                //Store the connection
                String hash = generateConnectionHash(sourceNodeId, targetNodeId);
                connections.put(hash, line);

                }
                break;
            case NONE:
                connectAlert(ConnectionMode.NONE);
                break;
            case DISCONNECT:
                if (!NodeUtils.disconnect(nodeMap.get(sourceNodeId), nodeMap.get(targetNodeId)) &&
                        !NodeUtils.disconnect(nodeMap.get(targetNodeId), nodeMap.get(sourceNodeId))
                )
                {connectAlert(ConnectionMode.DISCONNECT);}
                else {
                    String hash = generateConnectionHash(sourceNodeId, targetNodeId);
                    ConnectLine line = connections.remove(hash);
                    if (line != null) {

                        // Get the parent container of the line
                        AnchorPane parent = (AnchorPane) line.getParent();

                        // remove the line from the parent container
                        parent.getChildren().remove(line);


                        Node sourceNode = nodeMap.get(sourceNodeId);
                        Node targetNode = nodeMap.get(targetNodeId);

                        sourceNode.listAdjoiningNodes();
                        targetNode.listAdjoiningNodes();
                    }
                }
                break;
        }
    }

    public void disconnect(String sourceId, String targetId) {
        if (!NodeUtils.disconnect(nodeMap.get(sourceId), nodeMap.get(targetId)) &&
                !NodeUtils.disconnect(nodeMap.get(targetId), nodeMap.get(sourceId))
        ) {
            connectAlert(ConnectionMode.DISCONNECT);
        } else {
            String hash = generateConnectionHash(sourceId, targetId);
            ConnectLine line = connections.remove(hash);
            if (line != null) {

                // Get the parent container of the line
                AnchorPane parent = (AnchorPane) line.getParent();

                // remove the line from the parent container
                parent.getChildren().remove(line);
            }

        }

    }

    private Shape getTargetShape() {
        return Objects.requireNonNull(this.targetShape);

    }

    private Shape getSourceShape() {
        return Objects.requireNonNull(this.sourceShape);
    }


    public void setConnectMode(Boolean mode) {
        connectMode = mode;
    }

    public Boolean getConnectMode() {
        return connectMode;
    }

    private boolean isViableConnection(String sourceId, String targetId) {

        return !sourceId.equals(targetId);
    }

    public void resetConnector() {
        sourceNodeId = null;
        targetNodeId = null;
        connectMode = false;
        connectType = ConnectionMode.NONE;
    }

    public void setTargetNodeId(String TargetNodeId) {
        this.targetNodeId = TargetNodeId;
    }

    public void setSourceNodeId(String SourceNodeId) {
        this.sourceNodeId = SourceNodeId;
    }

    public void setConnectType(ConnectionMode connectionMode) {
        this.connectType = connectionMode;
    }

    public void connectAlert(ConnectionMode connectionMode) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Connection");


        switch (connectionMode) {
            case BEFORE , AFTER:
                // alert invalid connection
                alert.setHeaderText("Invalid Connection");
                alert.setContentText("Check connection type and try again");
                break;
            case NONE:
                // show invalid connection
                alert.setHeaderText("Invalid Connection");
                alert.setContentText("Generic invalid connection");
                break;
            case DISCONNECT:
                // show invalid disconnect
                alert.setHeaderText("Invalid Disconnect");
                alert.setContentText("Check if nodes are connected and try again");

                break;
        }

        alert.showAndWait();
    }

    public void setSourceShape(Shape shape) {
        this.sourceShape = shape;
    }

    private String generateConnectionHash(String sourceId, String targetId) {
        // Ensure the order of IDs does not affect the hash
        return Stream.of(sourceId, targetId)
                .sorted()
                .collect(Collectors.joining("-"));
    }
}
