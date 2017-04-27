package com.drk.tools.gplannercore.planner.search.unifier;

import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.state.Transition;

import java.util.HashSet;
import java.util.Set;

class Application {

    private final Set<Statement> positive;
    private final Set<Statement> negative;

    Application(Set<Statement> positive, Set<Statement> negative) {
        this.positive = positive;
        this.negative = negative;
    }

    boolean isApplicable(Transition transition) {
        if (transition.isNoop()) {
            return false;
        }
        Set<Statement> negativeContained = containedNegativePreconditions(transition);
        return containsAllPositivePreconditions(transition)
                && (negativeContained.isEmpty() || negative.containsAll(negativeContained));
    }

    private boolean containsAllPositivePreconditions(Transition transition) {
        StateTransition stateTransition = transition.stateTransition;
        return positive.containsAll(stateTransition.getPositivePreconditions());
    }

    private Set<Statement> containedNegativePreconditions(Transition transition) {
        StateTransition stateTransition = transition.stateTransition;
        Set<Statement> negativeContained = new HashSet<>();
        for (Statement statement : stateTransition.getNegativePreconditions()) {
            if (positive.contains(statement)) {
                negativeContained.add(statement);
            }
        }
        return negativeContained;
    }
}
