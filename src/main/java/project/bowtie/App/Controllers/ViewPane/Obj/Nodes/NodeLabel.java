package project.bowtie.App.Controllers.ViewPane.Obj.Nodes;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import project.bowtie.App.Controllers.ViewPane.Menus.ViewOption;
import project.bowtie.Model.BTmodel.Nodes.NodeDetail;
import project.bowtie.Model.BTmodel.Nodes.NodeType;
import project.bowtie.App.Controllers.ViewPane.Obj.Text.Dialogs;


import javafx.scene.control.*;
import javafx.util.Pair;

import java.util.*;

public class NodeLabel extends Label{

    private Shape shape;
    private NodeDetail nodeDetail;
    NodeType type;

    int offsetY;
    int offsetX;

    double dragOffsetY;
    double dragOffsetX;

    private ViewOption viewOption;
    private boolean drag = false;


    public NodeLabel(String text, Shape shape, NodeDetail nodeDetail, NodeType type) {
        super(text);
        // if nodeDetail is name handle name init, if nodeDetail is description handle description init
        this.shape = shape;
        this.type = type;
        if (nodeDetail == NodeDetail.NAME) {
            // if nodeDetail is name handle name init

            if (!drag){
                initNameLabelHandlers();
                this.viewOption = ViewOption.ALWAYS;

            }

        }
        if (nodeDetail == NodeDetail.DESCRIPTION) {

            if (!drag){
                initDescriptionLabelHandlers();
                this.viewOption = ViewOption.ON_HOVER;
            }
        }
        if (nodeDetail == NodeDetail.SCORE) {

            if (!drag){
                initScoreLabelHandlers();
                this.viewOption = ViewOption.NEVER;
            }
        }

        if (this.type == NodeType.AND) {
            this.setVisible(false);
        }

    }

    private void setOnHover(){

        this.setVisible(false); // Initially invisible

        shape.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            this.setVisible(false);
        });

        shape.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            this.setVisible(true);

        });

    }

    private void setOn() {
        this.setVisible(true); // Initially visible
        shape.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            this.setVisible(true);
        });

        shape.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            this.setVisible(true);
        });
    }

    private void setOff() {
        this.setVisible(false); // Initially invisible
        shape.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            this.setVisible(false);
        });

        shape.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            this.setVisible(true);
        });
    }

    private void setDrag() {

        // Set mouse pressed event
        this.setOnMousePressed(event -> {
            // Calculate the offset from the top-left corner of the rectangle
            dragOffsetX = event.getSceneX() - this.getLayoutX();
            dragOffsetY = event.getSceneY() - this.getLayoutY();
        });

        // Set mouse dragged event
        this.setOnMouseDragged(event -> {
            // Update the position of the rectangle
            this.setLayoutX(event.getSceneX() - dragOffsetX);
            this.setLayoutY(event.getSceneY() - dragOffsetY);
        });
    }

    public void toggleLabelVisibility(ViewOption viewOption) {

        System.out.println(viewOption);
        switch(viewOption) {
            case ALWAYS -> {

                this.setOn();
                this.viewOption = ViewOption.ALWAYS;
            }
            case ON_HOVER -> {

                this.setOnHover();
                this.viewOption = ViewOption.ON_HOVER;
            }
            case NEVER -> {

                this.setOff();
                this.viewOption = ViewOption.NEVER;
            }
        }
    }

    private void initScoreLabelHandlers() {

        Point2D offsets = calculateScoreOffsets(type);
        offsetY = (int) offsets.getY();
        offsetX = (int) offsets.getX();

        this.setLayoutY(shape.getLayoutY() + offsetY);

        this.setLayoutX(shape.getLayoutX() + offsetX);

        this.setOnHover();
        this.setDrag();

        setHandlers();
    }



    private void initNameLabelHandlers() {

        Point2D offsets = calculateNameOffsets(type);
        offsetY = (int) offsets.getY();
        offsetX = (int) offsets.getX();

        this.setLayoutY(shape.getLayoutY() + offsetY);

        this.setLayoutX(shape.getLayoutX() + offsetX);

        setHandlers();


    }

    private void initDescriptionLabelHandlers() {

        Point2D offsets = calculateDescriptionOffsets(type);
        offsetY = (int) offsets.getY();
        offsetX = (int) offsets.getX();

        this.setLayoutY(shape.getLayoutY() + offsetY);

        this.setLayoutX(shape.getLayoutX() + offsetX);

        this.setOnHover();
        this.setDrag();

        setHandlers();

    }

    private void setHandlers() {
        shape.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            // Example of providing visual feedback during dragging
            this.setVisible(false);

        });

        shape.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {

            this.setLayoutY(shape.getLayoutY() + offsetY);
            this.setLayoutX(shape.getLayoutX() + offsetX);

            if (this.viewOption == ViewOption.ON_HOVER) {
                this.setOnHover();
            }
            if (this.viewOption == ViewOption.ALWAYS) {
                setOn();
            }
            if (this.viewOption == ViewOption.NEVER) {
                setOff();
            }
        });
    }



    public void setNodeLabelText(String text) {
        super.setText(text);

    }



    // position offsets for the labels
    private Point2D calculateNameOffsets(NodeType type) {
        // set offsets for all different types of nodes
        double offsetY;
        double offsetX;

        switch(type){
            case TOP_EVENT -> {
                offsetY = 135;
                offsetX = 30;
                this.setStyle("-fx-background-color: white;");
            }
            case THREAT -> {
                offsetY = 135;
                offsetX = 30;
            }
            case VULNERABILITY -> {
                offsetY = -20;
                offsetX = -40;
                this.setStyle("-fx-background-color: white;");
            }
            case COUNTER_MITIGATION -> {
                offsetY = -20;
                offsetX = -40;
            }
            case MITIGATION -> {
                offsetY = -10;
                offsetX = -40;
            }
            case EXPOSURE -> {
                offsetY = -10;
                offsetX = -30;
            }
            case ACTION -> {
                offsetY = 0;
                offsetX = 0;
            }
            case AND -> {
                offsetY = 0;
                offsetX = 0;
                this.setVisible(false);
            }
            default -> {
                offsetY = 135;
                offsetX = 30;
            }

        }
        return new Point2D(offsetX, offsetY);
    }

    // position offsets for the labels
    private Point2D calculateDescriptionOffsets(NodeType type) {

        offsetY = 40;
        offsetX = 40;
        switch(type) {
            case ACTION -> {
                offsetY = 80;
                offsetX = 80;
            }
        }
        return new Point2D(offsetX, offsetY);
    }

    private Point2D calculateScoreOffsets(NodeType type) {

        offsetY = 80;
        offsetX = 80;
        switch(type) {
            case ACTION -> {
                offsetY = 80;
                offsetX = 80;
            }
        }
        return new Point2D(offsetX, offsetY);
    }


    public static List<String> scoreThreatLabel() {

        List<String> scores = new ArrayList<>(Arrays.asList("None", "None", "None")); // Default scores

        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = Dialogs.threatDialog();

        // Show the dialog and capture the result.
        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(score -> {
            // Split the result into individual scores
            String[] resultScores = score.getValue().split(",");
            // Check if resultScores array has the expected length of 3
            if (resultScores.length == 3) {
                scores.set(0, resultScores[0]);
                scores.set(1, resultScores[1]);
                scores.set(2, resultScores[2]);
            }
        });

        return scores;
    }

    public static List<String> scoreVulnLabel() {
        // Default scores for each metric

        // Default scores for each metric
        List<String> scores = new ArrayList<>(Arrays.asList("None", "None", "None", "None"));

        javafx.scene.control.Dialog<List<String>> dialog = Dialogs.vulnerabilityDialog();

        // Show the dialog and capture the result.
        Optional<List<String>> result = dialog.showAndWait();

        result.ifPresent(chosenScores -> {
            // Update the scores with the chosen values
            scores.clear();
            scores.addAll(chosenScores);
        });

        return scores; // This will return either the selected scores or the default "None" scores
    }

    public static List<String> scoreExpLabel() {
        // Default scores for each category
        List<String> scores = new ArrayList<>(Arrays.asList("None", "None", "None", "None", "None", "None", "None"));

        javafx.scene.control.Dialog<List<String>> dialog = Dialogs.exposureDialog();

        // Show the dialog and capture the result.
        Optional<List<String>> result = dialog.showAndWait();

        result.ifPresent(chosenScores -> {
            // Update the scores with the chosen values
            scores.clear();
            scores.addAll(chosenScores);
        });

        return scores; // This will return either the selected scores or the default "None" scores
    }

    public static List<String> scoreMitigatorsLabel() {
        // Default scores for each category
        List<String> scores = new ArrayList<>(Arrays.asList("None", "None","None","None"));

        javafx.scene.control.Dialog<List<String>> dialog = Dialogs.effectivenessDifficultyDialog();

        // Show the dialog and capture the result.
        Optional<List<String>> result = dialog.showAndWait();

        result.ifPresent(chosenScores -> {
            // Update the scores with the chosen values
            scores.clear();
            scores.addAll(chosenScores);
        });

        return scores; // This will return either the selected scores or the default "None" scores
    }

    public void toggleLabelVisibility() {
        this.setVisible(!this.isVisible());
    }

}
