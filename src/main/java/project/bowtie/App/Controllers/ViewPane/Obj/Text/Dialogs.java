package project.bowtie.App.Controllers.ViewPane.Obj.Text;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import java.util.Arrays;
import java.util.List;

/**
 * A class that provides dialog options for scoring different aspects of a threat model.
 */
public class Dialogs {

    // dialog option for vulnerability score
    public static javafx.scene.control.Dialog<List<String>> vulnerabilityDialog() {

        // Vulnerability metrics: Attack Vector, Attack Complexity, Privileges Required, User Interaction
        // Create the custom dialog.
        javafx.scene.control.Dialog<List<String>> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Vulnerability Score");

        // Set the button types.
        ButtonType scoreButtonType = new ButtonType("Score", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(scoreButtonType, ButtonType.CANCEL);

        // Create the exploitability metrics choices.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ChoiceBox<String> attackVectorChoice = new ChoiceBox<>();
        attackVectorChoice.getItems().addAll("Undefined", "Network", "Adjacent Network", "Local", "Physical");

        ChoiceBox<String> attackComplexityChoice = new ChoiceBox<>();
        attackComplexityChoice.getItems().addAll("Undefined","Low", "High");

        ChoiceBox<String> privilegesRequiredChoice = new ChoiceBox<>();
        privilegesRequiredChoice.getItems().addAll("None", "Low", "High");

        ChoiceBox<String> userInteractionChoice = new ChoiceBox<>();
        userInteractionChoice.getItems().addAll("None", "Required");

        // Set default values for the choice boxes to "None"
        attackVectorChoice.setValue("Undefined");
        attackComplexityChoice.setValue("Undefined");
        privilegesRequiredChoice.setValue("None");
        userInteractionChoice.setValue("None");

        // Add the choices to the grid
        grid.add(new Label("Attack Vector:"), 0, 0);
        grid.add(attackVectorChoice, 1, 0);
        grid.add(new Label("Attack Complexity:"), 0, 1);
        grid.add(attackComplexityChoice, 1, 1);
        grid.add(new Label("Privileges Required:"), 0, 2);
        grid.add(privilegesRequiredChoice, 1, 2);
        grid.add(new Label("User Interaction:"), 0, 3);
        grid.add(userInteractionChoice, 1, 3);

        dialog.getDialogPane().setContent(grid);
        // Convert the result to a List<String> when the score button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == scoreButtonType) {
                return Arrays.asList(
                        attackVectorChoice.getValue(),
                        attackComplexityChoice.getValue(),
                        privilegesRequiredChoice.getValue(),
                        userInteractionChoice.getValue()
                );
            }
            return null;
        });
        return dialog;
    }

    // dialog option for threat score
    public static  Dialog<Pair<String, String>> threatDialog(){

        //Threat categories: Confidentiality, Integrity, Availability

        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Theat Score");

        // Set the button types.
        ButtonType scoreButtonType = new ButtonType("Score", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(scoreButtonType, ButtonType.CANCEL);

        // Create the confidentiality, integrity, and availability labels and choices.
        // ALSO Non-repudiation, Authentication, Authorization, Integrity, Confidentiality, Availability
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ChoiceBox<String> confidentialityChoice = new ChoiceBox<>();
        confidentialityChoice.getItems().addAll("None", "Low", "High");

        ChoiceBox<String> integrityChoice = new ChoiceBox<>();
        integrityChoice.getItems().addAll("None", "Low", "High");

        ChoiceBox<String> availabilityChoice = new ChoiceBox<>();
        availabilityChoice.getItems().addAll("None", "Low", "High");

        grid.add(new Label("Confidentiality:"), 0, 0);
        grid.add(confidentialityChoice, 1, 0);
        grid.add(new Label("Integrity:"), 0, 1);
        grid.add(integrityChoice, 1, 1);
        grid.add(new Label("Availability:"), 0, 2);
        grid.add(availabilityChoice, 1, 2);

        dialog.getDialogPane().setContent(grid);

        integrityChoice.setValue("None");
        confidentialityChoice.setValue("None");
        availabilityChoice.setValue("None");


        // Convert the result to a String when the score button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == scoreButtonType) {
                // Retrieve values from choice boxes, which will be "None" by default if not changed by the user
                String confidentiality = confidentialityChoice.getValue();
                String integrity = integrityChoice.getValue();
                String availability = availabilityChoice.getValue();
                // Combine the values into a single String, separated by commas
                return new Pair<>("CIA", String.join(",", confidentiality, integrity, availability));
            }
            return null;
        });

        return dialog;
    }

    // dialog option for Exposure score
    public static javafx.scene.control.Dialog<List<String>> exposureDialog(){

        // Exposure categories: Financial, Operational, Legal, Strategic, Environmental, Health & Safety, Reputation

        // Create the custom dialog.
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Exposure Score");

        // Set the button types.
        ButtonType scoreButtonType = new ButtonType("Score", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(scoreButtonType, ButtonType.CANCEL);

        // Create the exposure categories choices.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ChoiceBox<String> financialChoice = new ChoiceBox<>();
        financialChoice.getItems().addAll("None", "Negligible", "Moderate", "Critical", "Catastrophic");

        ChoiceBox<String> operationalChoice = new ChoiceBox<>();
        operationalChoice.getItems().addAll("None", "Negligible", "Moderate", "Critical", "Catastrophic");

        ChoiceBox<String> legalChoice = new ChoiceBox<>();
        legalChoice.getItems().addAll("None", "Negligible", "Moderate", "Critical", "Catastrophic");

        ChoiceBox<String> strategicChoice = new ChoiceBox<>();
        strategicChoice.getItems().addAll("None", "Negligible", "Moderate", "Critical", "Catastrophic");

        ChoiceBox<String> environmentalChoice = new ChoiceBox<>();
        environmentalChoice.getItems().addAll("None", "Negligible", "Moderate", "Critical", "Catastrophic");

        ChoiceBox<String> healthSafetyChoice = new ChoiceBox<>();
        healthSafetyChoice.getItems().addAll("None", "Negligible", "Moderate", "Critical", "Catastrophic");

        ChoiceBox<String> reputationChoice = new ChoiceBox<>();
        reputationChoice.getItems().addAll("None", "Negligible", "Moderate", "Critical", "Catastrophic");

        grid.add(new Label("Financial:"), 0, 0);
        grid.add(financialChoice, 1, 0);
        grid.add(new Label("Operational:"), 0, 1);
        grid.add(operationalChoice, 1, 1);
        grid.add(new Label("Legal:"), 0, 2);
        grid.add(legalChoice, 1, 2);
        grid.add(new Label("Strategic:"), 0, 3);
        grid.add(strategicChoice, 1, 3);
        grid.add(new Label("Environmental:"), 0, 4);
        grid.add(environmentalChoice, 1, 4);
        grid.add(new Label("Health & Safety:"), 0, 5);
        grid.add(healthSafetyChoice, 1, 5);
        grid.add(new Label("Reputation:"), 0, 6);
        grid.add(reputationChoice, 1, 6);

        dialog.getDialogPane().setContent(grid);

        financialChoice.setValue("None");
        operationalChoice.setValue("None");
        legalChoice.setValue("None");
        strategicChoice.setValue("None");
        environmentalChoice.setValue("None");
        healthSafetyChoice.setValue("None");
        reputationChoice.setValue("None");

        // Convert the result to a List<String> when the score button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == scoreButtonType) {
                return Arrays.asList(
                        financialChoice.getValue(),
                        operationalChoice.getValue(),
                        legalChoice.getValue(),
                        strategicChoice.getValue(),
                        environmentalChoice.getValue(),
                        healthSafetyChoice.getValue(),
                        reputationChoice.getValue()
                );
            }
            return null;
        });
        return dialog;

    }

    // dialog option for Mitigation score
    public static Dialog<List<String>> effectivenessDifficultyDialog() {

        // Mitigation categories: Effectiveness, Difficulty -> Potentially coverage??
        // Create the custom dialog.
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Effectiveness & Difficulty Score");

        // Set the button types.
        ButtonType scoreButtonType = new ButtonType("Score", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(scoreButtonType, ButtonType.CANCEL);

        // Create the grid to arrange the controls.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Create the choice boxes for effectiveness and difficulty.
        ChoiceBox<String> effectivenessChoice = new ChoiceBox<>();
        effectivenessChoice.getItems().addAll("None","Very Low", "Low", "Moderate", "High", "Very High");

        ChoiceBox<String> difficultyChoice = new ChoiceBox<>();
        difficultyChoice.getItems().addAll("None","Very Low", "Low", "Moderate", "High", "Very High");

        ChoiceBox<String> coverageChoice = new ChoiceBox<>();
        coverageChoice.getItems().addAll("N/A","None","Very Low", "Low", "Moderate", "High", "Very High");
        ChoiceBox<String> opportunityChoice = new ChoiceBox<>();
        opportunityChoice.getItems().addAll("N/A","None","Very Low", "Low", "Moderate", "High", "Very High");

        // Set default values
        effectivenessChoice.setValue("None");
        difficultyChoice.setValue("None");
        coverageChoice.setValue("N/A");
        opportunityChoice.setValue("N/A");

        // Add the choice boxes to the grid.
        grid.add(new Label("Effectiveness:"), 0, 0);
        grid.add(effectivenessChoice, 1, 0);
        grid.add(new Label("Difficulty:"), 0, 1);
        grid.add(difficultyChoice, 1, 1);
        grid.add(new Label("Coverage:"), 0, 2);
        grid.add(coverageChoice, 1, 2);
        grid.add(new Label("Opportunity:"), 0, 3);
        grid.add(opportunityChoice, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Convert the result when the score button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == scoreButtonType) {
                return Arrays.asList(
                        effectivenessChoice.getValue(),
                        difficultyChoice.getValue(),
                        coverageChoice.getValue(),
                        opportunityChoice.getValue()
                );
            }
            return null;
        });
        return dialog;
    }
}
