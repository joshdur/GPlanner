package com.drk.tools.gplannercore.core.search;

import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.state.State;

public interface Searcher {

    enum Type {
        FOR_ALL,
        FIRST,
    }

    void startSearch(Context context, State initialState, State finalState) throws SearchException;
}
