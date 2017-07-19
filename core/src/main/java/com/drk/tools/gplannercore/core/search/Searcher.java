package com.drk.tools.gplannercore.core.search;


import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.search.context.SearchContext;

public interface Searcher {

    void startSearch(SearchContext searchContext, State initialState, State finalState) throws SearchException;
}
