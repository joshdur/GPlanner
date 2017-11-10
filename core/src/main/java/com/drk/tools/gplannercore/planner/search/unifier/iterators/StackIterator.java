package com.drk.tools.gplannercore.planner.search.unifier.iterators;

import com.drk.tools.gplannercore.core.state.Transition;

import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

public class StackIterator implements Iterator<Transition> {

    private final Stack<Transition> transitions;

    public StackIterator(Collection<Transition> transitions) {
        this.transitions = new Stack<>();
        this.transitions.addAll(transitions);
    }

    @Override
    public boolean hasNext() {
        return !transitions.isEmpty();
    }

    @Override
    public Transition next() {
        return transitions.pop();
    }
}
