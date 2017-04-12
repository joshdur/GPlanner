package com.drk.tools.gplannercore.planner.search.context.heuristic;

import com.drk.tools.gplannercore.core.state.State;

public interface Score {

    int resolve(State state);
}
