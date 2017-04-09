package com.drk.tools.gplannercore.core.state;

public class Transition {

    public final int unifierCode;
    public final int variableStateCode;
    public final StateTransition stateTransition;

    public Transition(int unifierCode, int variableStateCode, StateTransition stateTransition) {
        this.unifierCode = unifierCode;
        this.variableStateCode = variableStateCode;
        this.stateTransition = stateTransition;
    }
}
