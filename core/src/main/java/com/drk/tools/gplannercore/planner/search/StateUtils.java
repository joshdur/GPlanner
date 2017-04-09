package com.drk.tools.gplannercore.planner.search;

import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.core.state.Statement;

class StateUtils {

    static void applyEffects(State state, StateTransition stateTransition) {
        for (Statement negative : stateTransition.getNegativeEffects()) {
            state.remove(negative);
        }
        for (Statement positive : stateTransition.getPositiveEffects()) {
            state.set(positive);
        }
    }

    static boolean validate(State newState, State finalState) {
        return newState.getStatements().containsAll(finalState.getStatements());
    }
}
