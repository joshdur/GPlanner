package com.drk.tools.gplannercore.planner.search.unifier.providers;

import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.state.Transition;
import com.drk.tools.gplannercore.planner.search.unifier.iterators.UnifierIterator;

import java.util.Iterator;

public class ContextIteratorProvider implements IteratorProvider{

    public final Context context;

    public ContextIteratorProvider(Context context) {
        this.context = context;
    }

    @Override
    public Iterator<Transition> getIterator() {
        return new UnifierIterator(context.getValidUnifiers());
    }
}
