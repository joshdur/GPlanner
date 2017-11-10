package com.drk.tools.gplannercore.core.search.context;

import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.Plan;
import com.drk.tools.gplannercore.core.search.SearchException;
import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.Transition;
import com.drk.tools.gplannercore.core.streams.GOutputStream;
import com.drk.tools.gplannercore.planner.search.unifier.SearchUnifier;
import com.drk.tools.gplannercore.planner.search.unifier.providers.ContextIteratorProvider;
import com.drk.tools.gplannercore.planner.search.unifier.providers.IteratorProvider;
import com.drk.tools.gplannercore.planner.search.unifier.providers.StackIteratorProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchContext {

    private final Context context;
    private final SearchTimer searchTimer;
    private final Effects effects;
    private final SafeStream safeStream;
    private final IteratorProvider iteratorProvider;

    private SearchContext(Builder builder) {
        this.context = builder.context;
        this.searchTimer = builder.searchTimer;
        this.effects = builder.effects;
        this.safeStream = builder.safeStream;
        this.iteratorProvider = builder.iteratorProvider;
    }

    public long remainingTime() {
        return searchTimer.getRemainingTime();
    }

    public void assertRemainingTime() throws SearchException {
        if (remainingTime() <= 0) {
            throw new SearchException("Timout");
        }
    }

    public State applyEffects(State state, Transition transition) throws SearchException {
        return effects.applyEffects(state, transition);
    }

    public void pushPlan(Plan plan) throws SearchException {
        safeStream.pushPlan(plan, remainingTime());
    }

    public boolean isCancelled() {
        return safeStream.isClosed();
    }

    public void close() throws SearchException {
        safeStream.close();
    }

    public boolean validate(State newState, State finalState) {
        return newState.getStatements().containsAll(finalState.getStatements());
    }

    public boolean existsInSequence(State newState, SearchNode node) {
        SearchNode currentNode = node;
        boolean exists = false;
        while (currentNode != null && !exists) {
            exists = currentNode.getState().equals(newState);
            currentNode = currentNode.getLastNode();
        }
        return exists;
    }

    public Plan recoverSequence(SearchNode theNode) {
        List<Transition> transitions = new ArrayList<>();
        SearchNode node = theNode;
        while (node != null) {
            if (node.getTransition() != null) {
                transitions.add(node.getTransition());
            }
            node = node.getLastNode();
        }
        Collections.reverse(transitions);
        return new Plan(transitions);
    }

    public SearchUnifier getUnifier(State state) {
        return new SearchUnifier(state, iteratorProvider.getIterator());
    }

    public Context getContext() {
        return context;
    }

    public static class Builder {
        private final Context context;
        private final SafeStream safeStream;
        private SearchTimer searchTimer;
        private Effects effects;
        private IteratorProvider iteratorProvider;

        public Builder(Context context, GOutputStream outputStream) {
            this.context = context;
            this.safeStream = new SafeStream(outputStream);
            this.searchTimer = new SearchTimer(Long.MAX_VALUE);
            this.effects = new Effects(context, false);
            this.iteratorProvider = new ContextIteratorProvider(context);
        }

        public Builder(SearchContext searchContext) {
            this.context = searchContext.context;
            this.safeStream = searchContext.safeStream;
            this.searchTimer = searchContext.searchTimer;
            this.effects = searchContext.effects;
            this.iteratorProvider = searchContext.iteratorProvider;
        }

        public Builder timeout(long timeout) {
            searchTimer = new SearchTimer(timeout);
            return this;
        }

        public Builder online(boolean isOnline) {
            effects = new Effects(context, isOnline);
            return this;
        }

        public Builder transitions(List<Transition> transitions) {
            iteratorProvider = new StackIteratorProvider(transitions);
            return this;
        }

        public SearchContext build() {
            return new SearchContext(this);
        }
    }
}
