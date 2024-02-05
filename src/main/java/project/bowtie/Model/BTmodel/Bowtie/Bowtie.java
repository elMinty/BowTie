package project.bowtie.Model.BTmodel.Bowtie;

import project.bowtie.Model.BTmodel.Nodes.*;


public class Bowtie {
    private Node topEvent;
    private AttackTree attackTree;
    private ConsequenceTree consequenceTree;

    public Bowtie(Node topEvent) {
        this.topEvent = topEvent;
        this.attackTree = AttackTree.getInstance(topEvent);
        this.consequenceTree = ConsequenceTree.getInstance(topEvent);

    }

    public Node getTopEvent() {
        return topEvent;
    }

    public void setTopEvent(Node topEvent) {
        this.topEvent = topEvent;
    }

    public AttackTree getAttackTree() {
        return attackTree;
    }

    public ConsequenceTree getConsequenceTree() {
        return consequenceTree;
    }

}