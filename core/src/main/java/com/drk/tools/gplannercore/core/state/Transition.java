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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Transition && stateTransition.equals(((Transition) obj).stateTransition);
    }

    @Override
    public int hashCode() {
        return stateTransition.hashCode();
    }
}
