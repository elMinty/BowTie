package project.bowtie.App.Controllers.ViewPane.Obj.Lines;

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

/**
 * A class that manages the connection between two nodes in the view pane.
 * Live Object that updates based off of right click
 */
public class Connector {
    private Map<String, Node> nodeMap;
    Map<String, ConnectLine> connections = new HashMap<>();
    private String sourceNodeId; // source node id
    private String targetNodeId; // target node id
    private Shape sourceShape; // source shape
    private Shape targetShape; // target shape
    private boolean connectMode = false; // connection mode
    private ConnectionMode connectType = ConnectionMode.NONE;

    /**
     * Constructor for the Connector class.
     * @param nodeMap A map of node IDs to Node objects.
     */
    public Connector(Map<String, Node> nodeMap) {
        resetConnector();
        this.nodeMap = nodeMap;
    }

    /**
     * Flushes the connector and connects the source and target shapes based on the connection mode.
     * @param targetShape The target shape to connect to.
     */
    public void flush(Shape targetShape) {
        this.targetShape = targetShape;
        // Ensure that the source and target nodes are not the same
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

    /**
     * Connects the source and target shapes based on the connection mode.
     * @param connectType The type of connection to make.
     */
    private void connect(ConnectionMode connectType) {
        switch (connectType) {
            case BEFORE:
                // Check if the connection is valid
                if (!NodeUtils.connect(nodeMap.get(targetNodeId), nodeMap.get(sourceNodeId))){
                    connectAlert(ConnectionMode.BEFORE);} // alert invalid connection
                else {
                // Instantiate ConnectLine and bind it to the shapes
                ConnectLine line = new ConnectLine(sourceShape, targetShape, ConnectionMode.BEFORE);

                //Store the connection
                String hash = generateConnectionHash(sourceNodeId, targetNodeId);
                connections.put(hash, line);
                }
                break;
            case AFTER:
                // Check if the connection is valid
                if (!NodeUtils.connect(nodeMap.get(sourceNodeId), nodeMap.get(targetNodeId))){connectAlert(ConnectionMode.AFTER); System.out.println("Invalid connection");}
                else {
                Node sourceNode = nodeMap.get(sourceNodeId);
                // Instantiate ConnectLine and bind it to the shapes
                ConnectLine line = new ConnectLine(sourceShape, targetShape, ConnectionMode.AFTER);
                //Store the connection
                String hash = generateConnectionHash(sourceNodeId, targetNodeId);
                connections.put(hash, line);
                }
                break;
            case MITIGATE:
                // Check if the connection is valid
                if (!NodeUtils.mitigate(nodeMap.get(sourceNodeId), nodeMap.get(targetNodeId))){connectAlert(ConnectionMode.MITIGATE);}
                else {
                    Node sourceNode = nodeMap.get(sourceNodeId);
                    // Instantiate ConnectLine and bind it to the shapes
                    ConnectLine line = new ConnectLine(sourceShape, targetShape, ConnectionMode.MITIGATE);
                    //Store the connection
                    String hash = generateConnectionHash(sourceNodeId, targetNodeId);
                    connections.put(hash, line);
                }
                break;
            case NONE:
                System.out.println("Invalid connection");
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
                    }
                }
                break;
        }
    }

    /**
     * Disconnects the source and target shapes based on the connection mode.
     * @param sourceId The ID of the source node.
     * @param targetId The ID of the target node.
     */
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

    /**
     * Checks if the connection between the source and target nodes are the same.
     * @param sourceId The ID of the source node.
     * @param targetId The ID of the target node.
     * @return True if the connection is viable, false otherwise.
     */
    private boolean isViableConnection(String sourceId, String targetId) {
        return !sourceId.equals(targetId);
    }

    /**
     * Resets the connector.
     */
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

    /**
     * Displays an alert based on the connection mode.
     * @param connectionMode The type of connection to alert.
     */
    public void connectAlert(ConnectionMode connectionMode) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Connection");

        switch (connectionMode) {
            case BEFORE , AFTER:
                // alert invalid connection
                alert.setHeaderText("Invalid Connection");
                alert.setContentText("Check connection type and try again");
                break;
            case MITIGATE:
                // show invalid mitigation
                alert.setHeaderText("Invalid Mitigation");
                alert.setContentText("Check if nodes are connected and try again");
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

    /**
     * Generates a unique hash for the connection between the source and target nodes.
     * @param sourceId The ID of the source node.
     * @param targetId The ID of the target node.
     * @return A unique hash for the connection.
     */
    private String generateConnectionHash(String sourceId, String targetId) {
        // Ensure the order of IDs does not affect the hash
        return Stream.of(sourceId, targetId)
                .sorted()
                .collect(Collectors.joining("-"));
    }

    public void setSourceShape(Shape sourceShape) {
        this.sourceShape = sourceShape;
    }

    /**
     * Loads the connection between the source and target nodes.
     * @param sourceId The ID of the source node.
     * @param targetId The ID of the target node.
     * @param sourceShape The source shape.
     * @param targetShape The target shape.
     */
    public void load_connection(String sourceId, String targetId, Shape sourceShape, Shape targetShape) {

        connectMode = true;
        this.sourceNodeId = sourceId;
        this.targetNodeId = targetId;
        this.sourceShape = sourceShape;
        this.targetShape = targetShape;
        this.connectType = ConnectionMode.BEFORE;
        flush(getTargetShape());
        connectMode = false;
    }

    public  void load_mitigation(String sourceId, String targetId, Shape sourceShape, Shape targetShape) {

        connectMode = true;
        this.sourceNodeId = sourceId;
        this.targetNodeId = targetId;
        this.sourceShape = sourceShape;
        this.targetShape = targetShape;
        this.connectType = ConnectionMode.MITIGATE;
        flush(getTargetShape());
        connectMode = false;
    }
}
