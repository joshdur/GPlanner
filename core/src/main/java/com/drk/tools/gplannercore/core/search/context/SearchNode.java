package com.drk.tools.gplannercore.core.search.context;

import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.Transition;

public interface SearchNode {

    State getState();

    SearchNode getLastNode();

    Transition getTransition();
}
