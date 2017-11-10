package com.drk.tools.gplannercore.planner.search.unifier.providers;

import com.drk.tools.gplannercore.core.state.Transition;
import com.drk.tools.gplannercore.planner.search.unifier.iterators.StackIterator;

import java.util.Iterator;
import java.util.List;

public class StackIteratorProvider implements IteratorProvider {

    public final List<Transition> transitions;

    public StackIteratorProvider(List<Transition> transitions) {
        this.transitions = transitions;
    }

    @Override
    public Iterator<Transition> getIterator() {
        return new StackIterator(transitions);
    }
}
