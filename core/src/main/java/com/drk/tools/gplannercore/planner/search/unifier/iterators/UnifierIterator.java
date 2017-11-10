package com.drk.tools.gplannercore.planner.search.unifier.iterators;

import com.drk.tools.gplannercore.core.main.BaseUnifier;
import com.drk.tools.gplannercore.core.state.Transition;

import java.util.Iterator;
import java.util.List;

public class UnifierIterator implements Iterator<Transition> {

    private final List<BaseUnifier> unifiers;

    public UnifierIterator(List<BaseUnifier> unifiers) {
        this.unifiers = unifiers;
    }

    @Override
    public Transition next() {
        BaseUnifier unifier = unifiers.get(0);
        Transition transition = unifier.next();
        if (!unifier.hasNext()) {
            unifiers.remove(unifier);
        }
        return transition;
    }

    @Override
    public boolean hasNext() {
        return !unifiers.isEmpty() && unifiers.get(0).hasNext();
    }
}
