package com.drk.tools.gplannercore.planner.search.context.heuristic;

import com.drk.tools.gplannercore.core.state.State;

public class DefaultScore implements Score {
    @Override
    public int resolve(State state) {
        return 0;
    }
}
