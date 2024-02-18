package project.bowtie.App.Controllers.ViewPane.Obj;

import project.bowtie.App.Controllers.ViewPane.Menus.ConnectionMode;
import project.bowtie.Model.BTmodel.Nodes.Node;

import java.util.Map;
import java.util.Objects;

public class Connector {
    private Map<String, Node> nodeMap;

    private String sourceNodeId;
    private String targetNodeId;

    private boolean connectMode = false;
    private ConnectionMode connectType = ConnectionMode.NONE;
    private boolean viableConnection = false;

    public Connector(Map<String, Node> nodeMap) {
        this.nodeMap = nodeMap;
    }

    // flush the connector -- this is called when the user clicks on a shape and connection mode is active
    public void flush() {

        // check if the connection is viable
        if (isViableConnection(sourceNodeId, targetNodeId) && connectMode) {
            // Additional actions to indicate the application is in connection mode, if necessary
            connect(connectType);
        }
        else {
            resetConnector();
        }
    }

    // connection type
    private void connect(ConnectionMode connectType) {
        switch (connectType) {
            case BEFORE:
                // Additional actions to indicate the application is in connection mode, if necessary
                break;
            case AFTER:
                // Additional actions to indicate the application is in connection mode, if necessary
                break;
            case NONE:
                // Additional actions to indicate the application is in connection mode, if necessary
                break;
            case DISCONNECT:
                // Additional actions to indicate the application is in connection mode, if necessary
                break;
        }
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

    public void setTargetNodeId(String sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }
    public void setSourceNodeId(String targetNodeId) {
        this.targetNodeId = targetNodeId;
    }

    public void setConnectType(ConnectionMode connectionMode) {
        this.connectType = connectionMode;
    }
}
