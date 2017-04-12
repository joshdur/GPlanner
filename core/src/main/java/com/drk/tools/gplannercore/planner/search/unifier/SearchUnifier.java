package com.drk.tools.gplannercore.planner.search.unifier;

import com.drk.tools.gplannercore.core.state.Transition;

import java.util.HashSet;
import java.util.Set;

public abstract class SearchUnifier {

    protected final Application application;

    SearchUnifier(Application application) {
        this.application = application;
    }

    public abstract Transition next();

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
