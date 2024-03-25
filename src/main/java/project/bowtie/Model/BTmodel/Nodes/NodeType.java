package project.bowtie.Model.BTmodel.Nodes;


/**
 * Enum for NodeType
 * -----------------
 * This enum is used to represent the different types of nodes that can be in the BowTie model.
 * The types are the top event, threat, mitigation, counter mitigation, exposure, action, vulnerability, and, and none.
 * -----------------
 * Top Event: The top event of the BowTie model (THREAT TYPE)
 * Threat: A threat to the system
 * Mitigation: A mitigation to an action
 * Counter Mitigation: A counter mitigation to a mitigation
 * Exposure: damaging consequences of a threat
 * Action: An action taken by an attacker
 * Vulnerability: A vulnerability in the system
 * And: An AND gate
 * None: No type
 *
 */
public enum NodeType {
    TOP_EVENT,
    THREAT,
    MITIGATION,
    COUNTER_MITIGATION,
    EXPOSURE,
    ACTION,
    VULNERABILITY,
    AND,
    NONE
}
