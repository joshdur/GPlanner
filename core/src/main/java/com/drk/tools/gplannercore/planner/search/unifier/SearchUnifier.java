package com.drk.tools.gplannercore.planner.search.unifier;

import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.Transition;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SearchUnifier {

    private final State state;
    private final Iterator<Transition> iterator;

    public SearchUnifier(State state, Iterator<Transition> iterator) {
        this.state = state;
        this.iterator = iterator;
    }

    public Transition next() {
        while (iterator.hasNext()) {
            Transition transition = iterator.next();
            if (isApplicable(transition)) {
                return transition;
            }
        }
        return null;
    }

    private boolean isApplicable(Transition transition) {
        return !transition.isNoop() && state.checkAll(transition.getPreconditions());
    }

    public Set<Transition> all() {
        Set<Transition> transitions = new HashSet<>();
        Transition transition = next();
        while (transition != null) {
            transitions.add(transition);
            transition = next();
        }
        return transitions;
    }

}
