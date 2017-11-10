package com.drk.tools.gplannercore.core.state;

import java.util.Set;

public class Transition {

    private final static int NOOP_CODE = -7;

    public final int unifierCode;
    public final int variableStateCode;
    public final StateTransition stateTransition;

    public static Transition noop(StateTransition transition) {
        return new Transition(NOOP_CODE, 0, transition);
    }

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

    public boolean isNoop() {
        return unifierCode == NOOP_CODE;
    }

    public Set<Statement> getPreconditions(){
        return stateTransition.getPreconditions();
    }

    public Set<Statement> getEffects(){
        return stateTransition.getEffects();
    }
}
