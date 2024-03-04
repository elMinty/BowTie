package project.bowtie.Model.BTmodel.Nodes;

import java.util.ArrayList;
import java.util.List;



public class logicPath {

    enum logicType {
        AND, OR, SERIAL, MITIGATE
    }

    public boolean logic = false;
    public List<logicPath> containsPath = null;
    public List<Node> nodes = new ArrayList<>();
    public Node origin = null;

    public logicPath(logicType type, List<Node> nodes) {
        this.logic = true;
    }

    public void setOrigin(Node origin) {
        this.origin = origin;
    }

    public void addPath(logicPath path) {
        this.containsPath.add(path);
    }


    public void removePath(logicPath path) {
        this.containsPath.remove(path);
    }

    public void setLogic(boolean logic) {
        this.logic = logic;
    }

    public boolean getLogic() {
        return this.logic;
    }



    public List<logicPath> getContainsPath() {
        return this.containsPath;
    }

    public Node getOrigin() {
        return this.origin;
    }

    public void setContainsPath(List<logicPath> containsPath) {
        this.containsPath = containsPath;
    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }

    public void removeNode(Node node) {
        this.nodes.remove(node);
    }

    public List<Node> getNodes() {
        return this.nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public void clearNodes() {
        this.nodes.clear();
    }

    public void clearContainsPath() {
        this.containsPath.clear();
    }






}
