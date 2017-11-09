package com.drk.tools.gplannercore.planner;

import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.search.SearchException;
import com.drk.tools.gplannercore.core.search.Searcher;
import com.drk.tools.gplannercore.core.search.context.SearchContext;
import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.streams.GOutputStream;
import com.drk.tools.gplannercore.core.streams.PlanBuffer;

public class Planner {

    private final Searcher searcher;
    private final int bufferSize;
    private final boolean isOnline;
    private final long timeout;

    public Planner(Searcher searcher, int bufferSize) {
        this(searcher, bufferSize, false, Long.MAX_VALUE);
    }

    public Planner(Searcher searcher, int bufferSize, boolean isOnline, long timeout) {
        this.searcher = searcher;
        this.bufferSize = bufferSize;
        this.isOnline = isOnline;
        this.timeout = timeout;
    }

    public PlanStream search(Context context, State initState, State finalState) {
        PlanBuffer planBuffer = new PlanBuffer();
        PlanStream planStream = new PlanStream(planBuffer.inputStream(), bufferSize);
        SearchContext searchContext = buildSearchContext(context, planBuffer.outputStream());
        SearchData searchData = new SearchData(searchContext, initState, finalState);
        Worker worker = new Worker(searcher, searchData, planStream);
        worker.start();
        return planStream;
    }

    private SearchContext buildSearchContext(Context context, GOutputStream outputStream) {
        return new SearchContext.Builder(context, outputStream)
                .online(isOnline)
                .timeout(timeout)
                .build();
    }

    private static class Worker extends Thread {

        private final Searcher searcher;
        private final SearchData searchData;
        private final PlanStream planStream;

        Worker(Searcher searcher, SearchData searchData, PlanStream planStream) {
            this.searcher = searcher;
            this.searchData = searchData;
            this.planStream = planStream;
        }

        @Override
        public void run() {
            try {
                searcher.startSearch(searchData.searchContext, searchData.initState, searchData.finalState);
            } catch (SearchException e) {
                planStream.setError(e);
            }
        }
    }

    private static class SearchData {

        private final SearchContext searchContext;
        private final State initState;
        private final State finalState;

        SearchData(SearchContext searchContext, State initState, State finalState) {
            this.searchContext = searchContext;
            this.initState = initState;
            this.finalState = finalState;
        }
    }
}
