package com.drk.tools.gplannercore.planner.search.unifier;

import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.Transition;

class Application {

    private final State state;

    Application(State state) {
        this.state = state;
    }

    boolean isApplicable(Transition transition) {
        return !transition.isNoop() && state.checkAll(transition.stateTransition.getPreconditions());
    }

}
