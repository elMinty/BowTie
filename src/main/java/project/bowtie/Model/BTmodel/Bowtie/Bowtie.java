package project.bowtie.Model.BTmodel.Bowtie;

import project.bowtie.Model.BTmodel.Nodes.*;

/**
 * Bowtie class - provides the structure for the Bowtie model
 * Contains the Attack Tree and Consequence Tree
 * Used in the BowtieController for the Application
 */
public class Bowtie {
    private Node topEvent;
    public AttackTree attackTree;
    public ConsequenceTree consequenceTree;

    /**
     * Constructor for the Bowtie class
     *
     * @param topEvent The top event of the tree - type node
     */
    public Bowtie(Node topEvent) {
        this.topEvent = topEvent;
        this.attackTree = AttackTree.getInstance(topEvent);
        this.consequenceTree = ConsequenceTree.getInstance(topEvent);

        this.attackTree.setTopEvent(topEvent);
        this.consequenceTree.setTopEvent(topEvent);

    }

    // Getters and Setters
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