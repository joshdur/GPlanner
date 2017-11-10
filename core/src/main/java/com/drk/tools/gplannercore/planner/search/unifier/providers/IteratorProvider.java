package com.drk.tools.gplannercore.planner.search.unifier.providers;

import com.drk.tools.gplannercore.core.state.Transition;

import java.util.Iterator;

public interface IteratorProvider {

    Iterator<Transition> getIterator();
}
