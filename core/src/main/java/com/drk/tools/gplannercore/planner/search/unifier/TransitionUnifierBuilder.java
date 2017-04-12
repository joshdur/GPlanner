package com.drk.tools.gplannercore.planner.search.unifier;

import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.state.Transition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TransitionUnifierBuilder implements UnifierBuilder {

    private final List<Transition> transitions;

    public TransitionUnifierBuilder(List<Transition> transitions) {
        this.transitions = transitions;
    }

    @Override
    public SearchUnifier from(Set<Statement> positive, Set<Statement> negative) {
        Application application = new Application(positive, negative);
        return new TransitionUnifier(new ArrayList<>(transitions), application);
    }

}
