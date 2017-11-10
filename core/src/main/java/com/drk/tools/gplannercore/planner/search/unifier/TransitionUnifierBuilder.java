package com.drk.tools.gplannercore.planner.search.unifier;

import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.Transition;

import java.util.ArrayList;
import java.util.List;

public class TransitionUnifierBuilder implements UnifierBuilder {

    private final List<Transition> transitions;

    public TransitionUnifierBuilder(List<Transition> transitions) {
        this.transitions = transitions;
    }

    @Override
    public SearchUnifier from(State state) {
        Application application = new Application(state);
        return new TransitionUnifier(new ArrayList<>(transitions), application);
    }

}
